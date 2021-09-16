import React, {useEffect} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Table} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {getEntities} from './nosology.reducer';
import {useAppDispatch, useAppSelector} from 'app/config/store';

export const Nosology = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const nosologyList = useAppSelector(state => state.nosology.entities);
  const loading = useAppSelector(state => state.nosology.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="nosology-heading" data-cy="NosologyHeading">
        Nosologies
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Nosology
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {nosologyList && nosologyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {nosologyList.map((nosology, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${nosology.id}`} color="link" size="sm">
                      {nosology.id}
                    </Button>
                  </td>
                  <td>{nosology.name}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${nosology.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${nosology.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${nosology.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Nosologies found</div>
        )}
      </div>
    </div>
  );
};

export default Nosology;

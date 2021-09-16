import React, {useEffect} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {getEntity} from './nosology.reducer';
import {useAppDispatch, useAppSelector} from 'app/config/store';

export const NosologyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const nosologyEntity = useAppSelector(state => state.nosology.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nosologyDetailsHeading">Nosology</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{nosologyEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{nosologyEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/nosology" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/nosology/${nosologyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NosologyDetail;

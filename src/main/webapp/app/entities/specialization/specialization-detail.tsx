import React, {useEffect} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {getEntity} from './specialization.reducer';
import {useAppDispatch, useAppSelector} from 'app/config/store';

export const SpecializationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const specializationEntity = useAppSelector(state => state.specialization.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specializationDetailsHeading">Specialization</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{specializationEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{specializationEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/specialization" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/specialization/${specializationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecializationDetail;

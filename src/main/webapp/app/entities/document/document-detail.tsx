import React, {useEffect} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {getEntity} from './document.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';
import {useAppDispatch, useAppSelector} from 'app/config/store';

export const DocumentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const documentEntity = useAppSelector(state => state.document.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentDetailsHeading">Document</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{documentEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{documentEntity.name}</dd>
          <dt>
            <span id="path">Path</span>
          </dt>
          <dd>{documentEntity.path}</dd>
          <dt>
            <span id="uuid">Uuid</span>
          </dt>
          <dd>{documentEntity.uuid}</dd>
          <dt>
            <span id="changeTime">Change Time</span>
          </dt>
          <dd>
            {documentEntity.changeTime ? <TextFormat value={documentEntity.changeTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{documentEntity.user ? documentEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentDetail;

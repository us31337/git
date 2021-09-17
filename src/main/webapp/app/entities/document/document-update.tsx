import React, {useEffect, useState} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, CustomInput, FormGroup, Label, Row} from 'reactstrap';
import {ValidatedField, ValidatedForm} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {getUsers} from 'app/modules/administration/user-management/user-management.reducer';
import {createEntity, getEntity, reset, updateEntity} from './document.reducer';
import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';
import {useAppDispatch, useAppSelector} from 'app/config/store';

export const DocumentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);
  const [attachedFile, setAttachedFile] = useState(null);

  const users = useAppSelector(state => state.userManagement.users);
  const documentEntity = useAppSelector(state => state.document.entity);
  const loading = useAppSelector(state => state.document.loading);
  const updating = useAppSelector(state => state.document.updating);
  const updateSuccess = useAppSelector(state => state.document.updateSuccess);

  const handleClose = () => {
    props.history.push('/document');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.changeTime = convertDateTimeToServer(values.changeTime);
    values.file = attachedFile;
    const entity = {
      ...documentEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.userId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
        changeTime: displayDefaultDateTime(),
      }
      : {
        ...documentEntity,
        changeTime: convertDateTimeFromServer(documentEntity.changeTime),
        userId: documentEntity?.user?.id,
      };

  const handleFileChange = (event) => {
    let files = event.target.files;
    setAttachedFile(files.item(0));
  }

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feasBaseApp.document.home.createOrEditLabel" data-cy="DocumentCreateUpdateHeading">
            Create or edit a Document
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="document-id" label="ID"
                                        validate={{required: true}}/> : null}
              <ValidatedField
                label="Name"
                id="document-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: {value: true, message: 'This field is required.'},
                }}
              />
              <FormGroup>
                <Label for="exampleCustomFileBrowser">File upload</Label>
                <CustomInput
                  type="file"
                  id="exampleCustomFileBrowser"
                  name="customFile"
                  onChange={(event) => handleFileChange(event)}
                />
              </FormGroup>
              {/*<ValidatedField label="Path" id="document-path" name="path" data-cy="path" type="text" />
              <ValidatedField label="Uuid" id="document-uuid" name="uuid" data-cy="uuid" type="text" />
              <ValidatedField
                label="Change Time"
                id="document-changeTime"
                name="changeTime"
                data-cy="changeTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />*/}
              {/*<ValidatedField id="document-user" name="userId" data-cy="user" label="User" type="select">
                <option value="" key="0"/>
                {users
                  ? users.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.login}
                    </option>
                  ))
                  : null}
              </ValidatedField>*/}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/document" replace
                      color="info">
                <FontAwesomeIcon icon="arrow-left"/>
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit"
                      disabled={updating}>
                <FontAwesomeIcon icon="save"/>
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DocumentUpdate;

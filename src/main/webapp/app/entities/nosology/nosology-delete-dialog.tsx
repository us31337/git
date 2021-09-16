import React, {useEffect} from 'react';
import {RouteComponentProps} from 'react-router-dom';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {useAppDispatch, useAppSelector} from 'app/config/store';
import {deleteEntity, getEntity} from './nosology.reducer';

export const NosologyDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const nosologyEntity = useAppSelector(state => state.nosology.entity);
  const updateSuccess = useAppSelector(state => state.nosology.updateSuccess);

  const handleClose = () => {
    props.history.push('/nosology');
  };

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(nosologyEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="nosologyDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="feasBaseApp.nosology.delete.question">Are you sure you want to delete this Nosology?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-nosology" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default NosologyDeleteDialog;

import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Specialization from './specialization';
import SpecializationDetail from './specialization-detail';
import SpecializationUpdate from './specialization-update';
import SpecializationDeleteDialog from './specialization-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SpecializationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SpecializationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SpecializationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Specialization} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SpecializationDeleteDialog} />
  </>
);

export default Routes;

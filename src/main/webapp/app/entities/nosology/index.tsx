import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Nosology from './nosology';
import NosologyDetail from './nosology-detail';
import NosologyUpdate from './nosology-update';
import NosologyDeleteDialog from './nosology-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NosologyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NosologyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NosologyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Nosology} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NosologyDeleteDialog} />
  </>
);

export default Routes;

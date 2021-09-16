import React from 'react';
import {Switch} from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Specialization from './specialization';
import Nosology from './nosology';
import Document from './document';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}specialization`} component={Specialization} />
      <ErrorBoundaryRoute path={`${match.url}nosology`} component={Nosology} />
      <ErrorBoundaryRoute path={`${match.url}document`} component={Document} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

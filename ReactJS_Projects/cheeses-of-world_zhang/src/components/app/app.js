import React from 'react';

import {AppHeader} from '../app-header/app-header';
import {AppBody} from '../app-body/app-body';
import './app.css';

// Main app
export class App extends React.Component {
  render() {
    return (
      <div className="app">
        <AppHeader title="Cheeses of the World"></AppHeader> {/* Header*/}
        <AppBody></AppBody> {/*Body*/}
      </div>
    );
  }
}

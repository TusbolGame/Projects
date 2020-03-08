import React from 'react';
import ReactDOM from 'react-dom';
import {App} from './components/app/app';
import './index.css';
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import rootReducer from './reducers'

const store = createStore(rootReducer)



// App render
ReactDOM.render(
  <Provider store={store}>
      <App />
  </Provider>,
  document.getElementById('root')
);

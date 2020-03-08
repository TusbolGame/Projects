import React from 'react';
import './app-header.css';

// app header
export class AppHeader extends React.Component {
  render() {
    return (
    	<div className="app-header">
    		<h1 className="title">{this.props.title}</h1>
    		<div className="underline"></div>
    	</div>
      
    );
  }
}
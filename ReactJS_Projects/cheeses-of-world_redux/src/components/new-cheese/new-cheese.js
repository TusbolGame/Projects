import React from 'react';
import './new-cheese.css';
import {addCheese} from './../../actions'
import {connect} from 'react-redux';


// component for creating new cheese
 class NewCheese extends React.Component {

	constructor(props) {
		super(props)
		this.setState({
			cheeseName: '',
			cheeseNation: ''	
		})

	}

	state = { // state for component
		cheeseName: '',
		cheeseNation: ''
	}

	handleInputChange = (e) => { // update state from input
		this.setState({
			[e.target.name]: e.target.value
		});
	}


	handleSave = ( {e} ) => { // called when user click save button
		
	
		if (this.state.cheeseName.trim() === '' || this.state.cheeseNation === '') {
			return;
		}

		this.props.addCheeseItem(this.state)
		//this.props.onSave(this.state);
	    
		this.setState({
			cheeseName: '',
			cheeseNation: ''
		});
	}

	render() {
		return (
			<div className="new-cheese">
				<h1 className="title">Add New Cheese</h1>
				<div className="form">
		    		<input type="text" placeholder="Cheese Name" value={this.state.cheeseName} onChange={this.handleInputChange} name="cheeseName" />
		    		<input type="text" placeholder="Cheese Nation" value={this.state.cheeseNation} onChange={this.handleInputChange} name="cheeseNation" />
		    		<a href="#" className="button save" onClick={this.handleSave}>Save</a>
				</div>
			</div>
		  
		);
	}

}


const mapDispatchToProps = dispatch => ({
    addCheeseItem : cheese => dispatch(addCheese(cheese))
})


export default connect(
	null,
    mapDispatchToProps
)(NewCheese)




import React from 'react';
import './cheese-item.css';
import { connect } from 'react-redux';
import { deleteCheeseItem, applyCheeseItem } from '../../actions'

// represents cheese item
 class CheeseItem extends React.Component {

  static defaultProps = { // default props for component
    info: {
      id: 0,
      cheeseName: 'Cheese Name',
      cheeseNation: 'Cheese Nation'
    },
    onUpdate: () => console.warn('onUpdate not defined')
  }

  state = { // state for component that stores cheese name and chees nation
    editing: false,
    cheeseName: '',
    cheeseNation: ''
  }

  handleToggleEdit = () => { // called when user click edit button
    const {info} = this.props;
     this.setState({
      cheeseName: info.cheeseName,
      cheeseNation: info.cheeseNation
    })
    this.setState({ editing: true });
  }

  handleInputChange = (e) => { // update state from inputs
    this.setState({
      [e.target.name]: e.target.value
    });
  }

  handleApply = (e) => { // called when user click apply button
    if (this.state.cheeseName.trim() === '' || this.state.cheeseNation === '') {
      return;
    }

    const { info, onUpdate } = this.props; 
    
    this.props.onApplyItem({id : info.id, cheeseName:this.state.cheeseName, cheeseNation : this.state.cheeseNation});
    this.setState({ editing: false });
  }

  handleCancel = (e) => { // called when user click cancel button
    this.setState({ editing: false });
  }

  handleDelete = () => { // called when user click delete button
    const { info, onDelete } = this.props;
    this.props.onDeleteCheeseItem(info.id)

  }



  render() {

    const { editing } = this.state;

    if(editing) {
      // show edit ui
      return(
        <li className="cheese-item" >
          <div className="info">
            <input type="text" placeholder="Cheese Name" value={this.state.cheeseName} onChange={this.handleInputChange} name="cheeseName" /><br/>
            <input type="text" placeholder="Cheese Nation" value={this.state.cheeseNation} onChange={this.handleInputChange} name="cheeseNation" />
          </div>
          <div className="buttons">
            <a href="#" className="button apply" onClick={this.handleApply} >Apply</a>
            <a href="#" className="button cancel" onClick={this.handleCancel} >Cancel</a>
          </div>
        </li>
      );
    }

    const {cheeseName, cheeseNation} = this.props.info;

    // show info ui
    return (
      <li className="cheese-item" >
        <div className="info">
          <h1 className="cheese-name">{cheeseName}</h1>
          <h2 className="cheese-nation">{cheeseNation}</h2>
        </div>
        <div className="buttons">
          <a href="#" className="button edit" onClick={this.handleToggleEdit} >Edit</a>
          <a href="#" className="button delete" onClick={this.handleDelete} >Delete</a>
        </div>
      </li>

      );
  }
}



const mapDispatchToProps = dispatch => ({
    onDeleteCheeseItem : id => dispatch(deleteCheeseItem(id)),
    onApplyItem: cheese => dispatch(applyCheeseItem(cheese)),
})


export default connect(
	null,
    mapDispatchToProps
)(CheeseItem)



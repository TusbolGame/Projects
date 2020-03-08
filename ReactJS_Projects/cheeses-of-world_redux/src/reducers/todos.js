const todos = (state = [], action) => {
    switch(action.type){
        case 'ADD_TODO':
        console.log(action.type);
        return[
            ...state,
            {
                id: action.id,
                cheeseName: action.cheeseName,
                cheeseNation: action.cheeseNation
                
            }
        ]

        case 'DELETE_CHEESE':
            console.log(state)
        return  state.filter(cheeseItem => cheeseItem.id !== action.id)
       

         case 'APPLY_CHEESE':
            console.log(action.id)
            return  state.map(cheese => cheese.id !== action.id ? cheese : {...cheese, ...action})
       


        default:
            return state;

    }
}


export default todos



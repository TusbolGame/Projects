let nextCheese = 0;


export const addCheese = cheese => ({
    type: 'ADD_TODO',
    id: nextCheese++,
    cheeseName: cheese.cheeseName,
    cheeseNation: cheese.cheeseNation
})


export const deleteCheeseItem = id => ({
    type: 'DELETE_CHEESE',
    id: id
})


export const applyCheeseItem = cheese => ({
    type: 'APPLY_CHEESE',
    id: cheese.id,
    cheeseName: cheese.cheeseName,
    cheeseNation: cheese.cheeseNation
})




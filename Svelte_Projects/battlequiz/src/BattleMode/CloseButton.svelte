<svelte:head>
    <style>
        .mode-close-button{
            background-color: #D7D7D7;
            border-radius: 50px;
            color: #000000;
            font-size: 1.2em;
            font-weight: bold;
        }
        .close-container{
            float: right;
            width: 100%;
        }
        .close-container>button{
            float: right;
            top: 20px;
            right: 20px;
        }
    </style>
</svelte:head>

<script>
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import IconButton, {Icon} from '@smui/icon-button/bare.js';
    import '@smui/icon-button/bare.css';
    import Button, {Label} from '@smui/button/bare.js';
    import '@smui/button/bare.css'
</script>

<div class="close-container">
    <IconButton class="material-icons mode-close-button" on:click={()=>pop()}>clear</IconButton>
</div>


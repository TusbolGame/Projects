<svelte:head>
    <style>
        body{
            background: url('/images/users.png');
            background-repeat: repeat-y;
            background-size: cover;
        }
        .player-vs-container{
            height: 100%;
        }
    </style>
</svelte:head>
<script>
    import { onMount } from 'svelte';
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import { Col, Container, Row } from "sveltestrap";
    import TopPlayerInfo from './TopPlayerInfo.svelte';
    import BottomPlayerInfo from './BottomPlayer.svelte';
    import VSMark from './VSMark.svelte';

    onMount(() => {
        const interval = setInterval(() => {
            clearInterval(interval)
            push('/roundsplash')
        }, 5000);

        return () => clearInterval(interval);
    });

</script>


<div class="player-vs-container">
    <TopPlayerInfo/>
    <VSMark/>
    <BottomPlayerInfo/>
</div>

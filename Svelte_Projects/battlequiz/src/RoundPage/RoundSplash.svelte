<svelte:head>
    <style>
        .battle-info-items{
            /*opacity: 0.1;*/
            animation-duration: 4s;
            animation-name: fade;
        }
        @keyframes fade {
          from {opacity: 0.01;}
          to {opacity: 1;}
        }
    </style>
</svelte:head>

<script>

    import { onMount } from 'svelte';
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import { Col, Container, Row } from "sveltestrap";
    import TopicImage from '../BattleInfo/TopicImage.svelte'
    import RoundContainer from './RoundContainer.svelte'

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';
    import {opponent_score, my_score, round_number} from '../Store/DataStore.js';


    onMount(() => {
        const interval = setInterval(() => {
            clearInterval(interval)
            push('/battleround')
        }, 5000);

        return () => clearInterval(interval);
    });

  export let avatar_url = 'http://www.lte-esafety.co.uk/wp-content/uploads/2015/06/avatar.png';
</script>

<RoundContainer>
    <Container style="text-align: center">
        <TopicImage style="width: 60%;"/>
        <h3 class="battle-info-items" style="margin-top: 20px;">{$battle_quiz["battle_title"]}</h3>
        <h5 class="battle-info-items" style="margin-top: 10px;">EDUCATIONAL</h5>
        <h1 class="battle-info-items">ROUND {$round_number}</h1>
    </Container>
</RoundContainer>


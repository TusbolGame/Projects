<svelte:head>
  <style>
    .container{
      padding:0;
      margin:0;
      width:100% !important;
      max-width: 100% !important;
      min-width: 100% !important
    }
    .row{
        margin: 0px;
    }
    *{
        box-sizing: border-box;
    }
    body{
        background-color: #333333;
        padding: 0px;
    }
  </style>
</svelte:head>
<script>
    import { onMount } from 'svelte';
    import { Col, Container, Row } from "sveltestrap";
    import TopAppBar from "../BattleInfo/TopAppBar.svelte"
    import CategoryBody from "./CategoryBody.svelte"
    import SwitchButton from "./SwitchButton.svelte"

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';

    let players_data;
    let battle_data;
    let loaded = getData();

    async function getData(){
        const players_url = '/data/players.json';
        let res = await fetch(players_url);
        let json_data = await res.json();
        await players.set(json_data);
        await players.subscribe(value => {
            players_data = value;
            //console.log(players_data);
        });

        const battledata_url = '/data/battle_data.json';
        res = await fetch(battledata_url);
        json_data = await res.json();
        await battle_quiz.set(json_data);

        await battle_quiz.subscribe(await function(value) {
            battle_data = value;
            //console.log(battle_data);
        });
        return true;
    }

</script>

{#await loaded}
    <p>...waiting</p>

{:then number}
    <Container>
      <Row>
        <Col style="padding:0">
          <TopAppBar/>
          <CategoryBody/>
          <SwitchButton/>
        </Col>
      </Row>
    </Container>
{:catch error}
	<p style="color: red">{error.message}</p>
{/await}

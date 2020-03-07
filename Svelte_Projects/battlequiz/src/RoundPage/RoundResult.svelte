<svelte:head>
    <style>
        body{
            display: inline-table;
            height: 100vh;
            background-color: #111111;
            position: relative;

        }
        .win-score-container{
            background-color: #313131;
            color: #FFFFFF;
            border-radius: 8px;
        }
        .win-score-h{
            margin: 0;
        }
        .winner-avatar{
            border-radius: 50%;
            border: 3px solid #008000;
            width: 40%;
            height: 40%;
        }
        .winner-info{
            color: #008000;
        }
        .loster-avatar{
            border-radius: 50%;
            border: 3px solid #ff0000;
            width: 40%;
            height: 40%;
        }
        .loster-info{
            color: #ff0000;
        }
    </style>
</svelte:head>

<script>
    import { Col, Container, Row } from "sveltestrap";
    import { Button } from "sveltestrap";
    import { Progress } from 'sveltestrap';
    import Fab, {Label, Icon} from '@smui/fab/bare.js';
    import '@smui/fab/bare.css';

    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import { players } from '../Store/PlayerStore.js';
    import {my_score, opponent_score} from '../Store/DataStore.js';

    let win_countRate = $players["players"][0]["info"]["win_counter"] * 100 / 3;
    let avatar_urls = [$players["players"][0]["avatar"], $players["players"][1]["avatar"]];
    let scores = [$players["players"][0]["info"]["score"], $players["players"][1]["info"]["score"]];

    function rematch() {
        $players["players"][0]["info"]["score"] = 0;
        $players["players"][1]["info"]["score"] = 0;
        my_score.set(0);
        opponent_score.set(0);
        push('/pvp');
    }
    function another_opponent() {
        $players["players"][0]["info"]["score"] = 0;
        $players["players"][1]["info"]["score"] = 0;
        my_score.set(0);
        opponent_score.set(0);
        push('/battlemode');
    }
    function exit_battle(){
        $players["players"][0]["info"]["score"] = 0;
        $players["players"][1]["info"]["score"] = 0;
        my_score.set(0);
        opponent_score.set(0);
        push('/');
    }

</script>

<div style="height: 30px;"></div>

<Container class="win-score-container">
    <Row>
        <Col xs="6">
            <h6 class="win-score-h" style="padding-top: 20px;">Multiplayer</h6>
            <h6 class="win-score-h" style="padding-bottom: 20px;">Win counter:(0/{$players["players"][0]["info"]["win_counter"]})</h6>
        </Col>
        <Col xs="6" style="background-color: #484848">
            <Row style="height: 100%;">
                <Col xs="8" style="height: 0.6rem; margin:auto;">
                    <Progress style="height: 0.6rem;border-radius: 8px;" color="info" value={win_countRate} />
                </Col>
                <Col xs="4" style="padding: 0; margin: 0">
                    <img style="width: 100%; height: 100%;" src="/images/gift.png" alt="Gift Image"/>
                </Col>
            </Row>
        </Col>
    </Row>

</Container>
<div style="height: 30px;"></div>
<Container>
    <Row>
        <Col xs="5" style="text-align: right;">
            <img style="width: 80%;" src="/images/avatar.png" alt="Avatar Image"/>
            <div style="text-align: right; display: inline-block">
                <img class="loster-avatar" style="float:right;" src={avatar_urls[0]} alt="Avartar"/>
                <h4 style="width: 50%;float:right;font-weight: bold;padding-top: 15%; padding-right: 2%;" class="loster-info">{scores[0]}</h4>
            </div>

        </Col>
        <Col xs="2" style="vertical-align: middle; text-align: center; display: flex;">
            <div style="height: 30px; margin:auto;">
                <Icon class="material-icons" style="vertical-align: middle; color: #FFFFFF; font-size: 30px;">offline_bolt</Icon>
            </div>
        </Col>
        <Col xs="5" style="text-align: left;">
            <img style="width: 80%;" src="/images/avatar.png" alt="Avatar Image"/>
            <div style="text-align: left; display: inline-block">
                <img class="winner-avatar" style="float:left; margin-right: 2%;" src={avatar_urls[1]} alt="Avartar"/>
                <h4 style="width: 50%;float:left;font-weight: bold;padding-top: 15%; " class="winner-info">{scores[1]}</h4>
            </div>
        </Col>
    </Row>
    <Row>
        <Col xs="5" style="text-align: right;">
            <h5 class="loster-info">
                {$players["players"][0]["name"]}
            </h5>
            <h6 style="color: #808080;">
                {$players["players"][0]["level_title"]}
            </h6>
            <h6 style="color: #FFFFFF;">
                Level {$players["players"][0]["info"]["level"]}
            </h6>
        </Col>
        <Col xs="2" style="text-align: center; color: #808080">vs</Col>
        <Col xs="5" style="text-align: left;">
            <h5 class="winner-info">
                {$players["players"][1]["name"]}
            </h5>
            <h6 style="color: #808080;">
                {$players["players"][1]["level_title"]}
            </h6>
            <h6 style="color: #FFFFFF;">
                Level {$players["players"][1]["info"]["level"]}
            </h6>
        </Col>
    </Row>
    <Row style="text-align: center; color: #FFFFFF;">
        <Col>
            <h3>
                Your opponent wants a<br>rematch
            </h3>

            <Button color={"success"} on:click={rematch} style="width: 80%; margin-bottom: 20px;">
                Rematch
            </Button>
            <Button color={"success"} on:click={another_opponent} style="width: 80%; margin-bottom: 20px;">
                Another Opponent
            </Button>
            <Button color={"danger"} on:click={exit_battle} style="width: 80%; margin-bottom: 20px;">
                Not right now...
            </Button>
        </Col>
    </Row>
</Container>
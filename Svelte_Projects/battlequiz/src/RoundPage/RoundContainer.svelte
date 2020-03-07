<svelte:head>
    <style>
        body{
            display: inline-table;
            height: 100vh;
            background-color: #111111;
            position: relative;
        }
        .round-container{
            padding-top: 40px;
            color: #FFFFFF;
        }
        .info-bar{
            max-width: 100% !important;
            min-width: 100% !important;
        }
        .player-info{
            margin-top: 1rem;
        }
        .avatar{
            width: 100%;
        }
        .topic-image{
            float: none !important;
            width: 40% !important;
            margin-top: 20%;
            /*opacity: 0.1;*/
            animation-duration: 4s;
            animation-name: fade;
        }
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
    import { Progress } from 'sveltestrap';
    import { Col, Container, Row } from "sveltestrap";
    import { onMount } from 'svelte';
    import { writable } from 'svelte/store';

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';
    import { remaining_time, time_progress } from '../Store/DataStore.js';
    import { opponent_score, my_score} from '../Store/DataStore.js';

    import Avatar from '../Players/Avatar.svelte';
    import TopicImage from '../BattleInfo/TopicImage.svelte';

    let avatar_urls = [$players["players"][0]["avatar"], $players["players"][1]["avatar"]];
    let names = [$players["players"][0]["name"], $players["players"][1]["name"]];
    let scores = [$players["players"][0]["info"]["score"], $players["players"][1]["info"]["score"]];
    let level_titles = $players["players"][1]["level_title"];
    let levels = $players["players"][1]["info"]["level"];
    let nation_flag = $players["players"][1]["info"]["nation"];
    let comments = $players["players"][1]["info"]["comment"];
    let rates = $players["players"][1]["info"]["rate"];

</script>

<div class="round-container">
    <Progress style="height: 0.5rem;" color="info" value={$time_progress} />
    <Container class="info-bar">
        <Row>
            <Col xs="5" style="float: left; text-align: left;">
                <Row>
                    <Col xs="4" style="padding-right: 0px;">
                        <Avatar url={avatar_urls[0]}></Avatar>
                    </Col>
                    <Col xs="8" class="player-info">
                        <h6>
                            {names[0]}
                        </h6>
                        <h5 style="color: #FFFF00;">
                            {$my_score}
                        </h5>
                    </Col>
                </Row>
            </Col>
            <Col xs="2" style="text-align: center; padding-top: 1rem">
                <div style="font-size: 0.5em; color: #00B5D7">TIME LEFT</div>
                <div style="font-size: 1em; color: #00B5D7">{$remaining_time}</div>
            </Col>
            <Col xs="5">
                <Row>
                    <Col xs="8" class="player-info" style="text-align: right;">
                        <h6>
                            {names[1]}
                        </h6>
                        <h5 style="color: #FFFF00">
                            {$opponent_score}
                        </h5>
                    </Col>
                    <Col xs="4" style="padding-left: 0px;">
                        <Avatar url={avatar_urls[1]}></Avatar>
                    </Col>
                </Row>
            </Col>
        </Row>
    </Container>
    <slot/>
</div>

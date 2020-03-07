<svelte:head>
    <style>
        h2{
            color: #ffffff;
            text-align: center;
            padding-top: 30px;
            padding-bottom: 0px;
            margin-bottom: 0px;
        }
        h4{
            color:#fff;
            text-align: center;
            margin-bottom: 30px;
        }
        .container-padding{
            padding-left: 5%;
            padding-right: 5%;
        }
        img{
            width: 100%;
            float: right;
            border-radius: 10%;
        }
        .function-button{
            width: 100%;
            height: 30%;
        }
        .button-full-width{
            width: 100%;
            height: 100%;
        }
        .function-icon{
            float: left !important;
        }
        .function-span{
            float: left !important;

        }
        .percent-bar{
            position: relative;
            background-color: transparent;
            border: 1px solid #fff;
            height: 1.8rem;
            border-radius: 15px;
            width: 80%;
            margin:auto;
        }
        .percent-value{
            width: 100%;
            z-index: 4;
            position: absolute;
            height: 100%;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            text-align: center;
            color: #fff;
            margin-top: 0.2rem;
        }

        .info-bar{
            color: #fff;
            text-align: center;
            margin-top: 30px;
        }
        .middle-info-col{
            border-left: 2px solid #5F5F5F;
            border-right: 2px solid #5F5F5F;
        }
    </style>
</svelte:head>
<script>
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import { Col, Container, Row } from "sveltestrap";
    import { Progress } from 'sveltestrap';
    import ProgressBar from '@okrad/svelte-progressbar';

    import Fab, {Label, Icon} from '@smui/fab/bare.js';
    import '@smui/fab/bare.css';

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';

    import BattleTopic from './BattleTopic.svelte'
    import TopicImage from './TopicImage.svelte'

    let percentage = $players["players"][0]["info"]["question_comprate"];
    let level = $players["players"][0]["info"]["level"];
    let followers = $players["players"][0]["info"]["followers"];
    let next_title = $players["players"][0]["info"]["next_title"];

</script>

<Container>
    <BattleTopic battle_title={$battle_quiz["battle_title"]} battle_question={$battle_quiz["battle_subtitle"]}></BattleTopic>>

    <Row class="container-padding">
        <Col xs="6">
            <TopicImage/>
        </Col>
        <Col xs="6">
            <div class="margins function-button">
                <Fab class="button-full-width" style="background-color: #01D173; color: #fff" on:click={()=> push('/battlemode')} extended>
                    <Col xs="1" style="padding: 0px;">
                        <Icon class="material-icons function-icon">offline_bolt</Icon>
                    </Col>
                    <Col xs="11">
                        <Label class="function-span" style="font-weight: bold; font-size: 1.2em;">Play</Label>
                    </Col>
                </Fab>
            </div>
            <div class="margins function-button" style="margin-top: 5%; margin-bottom: 5%;">
                <a>
                <Fab class="button-full-width" style="background-color: #fff; color:#02CF73" extended>
                    <Col xs="1" style="padding: 0px;">
                        <Icon class="material-icons function-icon">add_circle</Icon>
                    </Col>
                    <Col xs="11">
                        <Label class="function-span" style="font-weight: bold; font-size: 1.2em;">Follow</Label>
                    </Col>
                </Fab>
                </a>
            </div>
            <div class="margins function-button">
                <Fab class="button-full-width" style="background-color: #fff; color: #9873E5;" extended>
                    <Col xs="1" style="padding: 0px;">
                        <Icon class="material-icons function-icon">brightness_auto</Icon>
                    </Col>
                    <Col xs="11">
                        <Label class="function-span" style="font-weight: bold; font-size: 1.2em;">Rankings</Label>
                    </Col>
                </Fab>
            </div>
        </Col>
    </Row>

    <Row>
        <Col>
            <h4 style="margin-top: 30px;">
                QUESTION COMPLETED
            </h4>
        </Col>
    </Row>

    <Row>
        <Col>
            <div class="progress percent-bar">
                <div class="progress-bar progress-bar-striped bg-success" style = "width:{percentage};" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">

                </div>
                <span class="percent-value">
                    {percentage}
                </span>
            </div>
        </Col>
    </Row>

    <Row class="info-bar">
        <Col xs="4">
            <h6>
                YOUR LEVEL
            </h6>
            <h3>
            {level}
            </h3>
        </Col>
        <Col xs="4" class="middle-info-col">
            <h6>
                FOLLOWERS
            </h6>
            <h3>{followers}</h3>
        </Col>
        <Col xs="4">
            <h6>
                NEXT TITLE AT LVL
            </h6>
            <h3>{next_title}</h3>
        </Col>

    </Row>
</Container>
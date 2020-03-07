<svelte:head>
    <style>
        .bottom-player-container{
            color: #FFFFFF;
            padding-left: 10% !important;
            padding-right: 10% !important;
            margin: 0px !important;
            max-width: 100% !important;
            padding-bottom: 1rem;
            min-height: 35% !important;
        }
        .nationality-img{
            border-radius: 50%;
            border: 2px solid #FFFFFF;
            width: 30px;
            height: 30px;
        }
        .bottom-player-rating{
            margin-bottom: 30px;
            width: 100%;
        }
        .bottom-rate-row .Rate{
            float:right;
        }
    </style>
</svelte:head>

<script>
    import { Col, Container, Row } from "sveltestrap";
    import Avatar from './Avatar.svelte'
    import Rate from './Rate.svelte'

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';

    let avatar_url = $players["players"][1]["avatar"];
    let name = $players["players"][1]["name"];
    let level_title = $players["players"][1]["level_title"];
    let level = $players["players"][1]["info"]["level"];
    let nation_flag = $players["players"][1]["info"]["nation"];
    let comment = $players["players"][1]["info"]["comment"];
    let rate = $players["players"][1]["info"]["rate"];


</script>

<Container class="bottom-player-container">

    <Row class="bottom-player-rating">
        <Col xs="6" style="margin-right: 50%;">
            <Row class="bottom-rate-row" style="display: block;">
                <div style="display:flow-root">
                    <h5 style="margin: 0; float: right;">
                        Qluzo
                    </h5>
                </div>
                <Rate length={3} showCount={false} value={rate} readonly={true}></Rate>
            </Row>
        </Col>
    </Row>
    <Row>
        <Col xs="8" style="text-align: right;">
            <h3>
                {name}
            </h3>
            <h5>
                {level_title}
            </h5>
            <h5>
                Level {level}
            </h5>
            <div style="display: inline-block;">
                <img alt="National Flag" src={nation_flag} class="nationality-img"/>
                <span style="font-size: 20px; vertical-align: middle; padding-left: 10px;">
                    {comment}
                </span>
            </div>
        </Col>
        <Col xs="4">
            <Avatar url={avatar_url}></Avatar>
        </Col>
    </Row>
</Container>
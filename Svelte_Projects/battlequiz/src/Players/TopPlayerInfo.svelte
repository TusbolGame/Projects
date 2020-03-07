<svelte:head>
    <style>
        .top-player-container{
            color: #FFFFFF;
            padding-left: 10% !important;
            padding-right: 10% !important;
            margin: 0px !important;
            max-width: 100% !important;
            min-height: 35% !important;
        }
        .top-player-info{
            margin-top: 1rem;
        }
        .nationality-img{
            border-radius: 50%;
            border: 2px solid #FFFFFF;
            width: 30px;
            height: 30px;
        }
        .top-player-rating{
            margin-top: 30px;
            width: 100%;
        }
    </style>
</svelte:head>

<script>
    import { Col, Container, Row } from "sveltestrap";
    import Avatar from './Avatar.svelte'
    import Rate from './Rate.svelte'

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';

    let avatar_url = $players["players"][0]["avatar"];
    let name = $players["players"][0]["name"];
    let level_title = $players["players"][0]["level_title"];
    let level = $players["players"][0]["info"]["level"];
    let nation_flag = $players["players"][0]["info"]["nation"];
    let comment = $players["players"][0]["info"]["comment"];
    let rate = $players["players"][0]["info"]["rate"];


</script>

<Container class="top-player-container">
    <Row>
        <Col xs="4">
            <Avatar url={avatar_url}></Avatar>
        </Col>
        <Col xs="8" class="top-player-info">
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
    </Row>
    <Row class="top-player-rating">
        <Col xs="6" style="margin-left: 50%">
            <Row style="display: block;">
                <h5 style="margin: 0;">
                    Qluzo
                </h5>
                <Rate length={3} showCount={false} value={rate} readonly={true}></Rate>
            </Row>

        </Col>
    </Row>
</Container>
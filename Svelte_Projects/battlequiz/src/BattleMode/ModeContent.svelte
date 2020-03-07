<svelte:head>
    <style>
        .battle-mode-content{
            position: absolute;
            bottom: 0px;
            right: 0px;
            top: 0px;
            left: 0px;
            background-color: #2E2E2EEE;
            color: white;
            width: 100%;
            height: 100%;
            z-index: 5;
        }

    </style>

</svelte:head>
<script>
    import { Col, Container, Row } from "sveltestrap";
    import { Button } from "sveltestrap";



    import CloseButton from './CloseButton.svelte'
    import BattleTopic from '../BattleInfo/BattleTopic.svelte'
    import TopicImage from '../BattleInfo/TopicImage.svelte'
    import ModeButtons from './ModeButtons.svelte'

    let success = 'success';
    let light = 'light';

    let clicked = 0;

</script>

<div class="battle-mode-content">
    <Container>
        <Row>
            <CloseButton/>
        </Row>
    </Container>
    <Container>
        <Row>
            <div style="width: 30%; margin: auto;">
                <TopicImage/>
            </div>
        </Row>
    </Container>
    <BattleTopic/>

    <ModeButtons/>
</div>


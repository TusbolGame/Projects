<svelte:head>
    <style>
        .mode-button-col-left{
            float: right;
            width: 80%;
            border-radius: 10px;
            color: #01D173;
            background-color: #FFFFFF;
        }
        .mode-button-col-right{
            float: left;
            width: 80%;
            border-radius: 10px;
            color: #FFFFFF;
            background-color: #01D173;
        }
    </style>
</svelte:head>

<script>
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'
    import { Col, Container, Row } from "sveltestrap";
    import Fab, {Label, Icon} from '@smui/fab/bare.js';
    import '@smui/fab/bare.css';

    let clicked = 0;
</script>

<Container>
    <Row>
        <Col xs="6">
            <Fab class="mode-button-col-left" on:click={()=> clicked++} extended>
                <Col xs="1" style="padding: 0px;">
                    <Icon class="material-icons function-icon">account_circle</Icon>
                </Col>
                <Col xs="11" style="padding-left: 10px;">
                    <Label class="function-span" style="line-height: normal">Single<br>Player</Label>
                </Col>
            </Fab>
        </Col>
        <Col xs="6">
            <Fab class="mode-button-col-right" on:click={()=>push('/pvp')} extended>
                <Col xs="1" style="padding: 0px;">
                    <Icon class="material-icons function-icon">group</Icon>
                </Col>
                <Col xs="11" style="padding-left: 10px;">
                    <Label class="function-span" style="line-height: normal">Random<br>Opponent</Label>
                </Col>
            </Fab>
        </Col>
    </Row>
</Container>
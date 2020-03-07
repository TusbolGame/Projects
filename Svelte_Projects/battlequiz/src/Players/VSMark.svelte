<svelte:head>
    <style>
        .vs-mark-container{
            color: #FFFFFF;
            min-height: 30% !important;
            padding: 0;
            margin: 0;
            min-width: 100% !important;
            text-align: center;
            align-items: center;
            display: flex;
            vertical-align: middle;
        }
        .vs-icon{
            font-size: 6em;
            border: 3px solid #FFFFFF;
            color: #000000;
            background-color: #FFFFFF;
            border-radius: 50%;
            margin: auto;
            z-index: 1;
        }
    </style>
</svelte:head>

<script>
    import { Col, Container, Row } from "sveltestrap";
    import Fab, {Label, Icon} from '@smui/fab/bare.js';
    import '@smui/fab/bare.css';
</script>

<Container class="vs-mark-container">
    <div style="height: 5px; background-color: #FFFFFF;position: absolute;z-index: 0;width: 100%">
    </div>
    <Icon class="material-icons vs-icon">offline_bolt</Icon>
</Container>
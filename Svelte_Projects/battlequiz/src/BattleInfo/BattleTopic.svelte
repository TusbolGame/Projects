
<script>
    import { Col, Container, Row } from "sveltestrap";
    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';
</script>

<Row>
    <Col>
        <h2>
            {$battle_quiz["battle_title"]}
        </h2>
        <h4>
            {$battle_quiz["battle_subtitle"]}
        </h4>
    </Col>
</Row>
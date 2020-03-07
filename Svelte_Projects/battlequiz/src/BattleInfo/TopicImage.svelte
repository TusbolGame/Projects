<svelte:head>
    <style>
        .topic-image{
            width: 100%;
            float: right;
            border-radius: 10%;
        }
    </style>
</svelte:head>

<script>
    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';
</script>

<img class="topic-image" alt="Basic Math Image" src={$battle_quiz["battle_image"]}/>
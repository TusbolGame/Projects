<svelte:head>
    <style>
        .battle-mode{
            position:relative;
        }
    </style>
</svelte:head>
<script>
    import BattleInfo from '../BattleInfo/BattleInfo.svelte'
    import ModeContent from './ModeContent.svelte'
</script>
<div class="battle-mode">
    <BattleInfo/>
    <ModeContent/>
</div>

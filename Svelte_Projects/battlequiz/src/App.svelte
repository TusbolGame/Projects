<svelte:head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        *{
            box-sizing: border-box;
        }
        body{
            margin: 0px !important;
            padding: 0px !important;
        }
    </style>
</svelte:head>
<script>

    import { Button } from 'sveltestrap';
    import Router from 'svelte-spa-router'

    import TopicCard from './Topics/TopicCard.svelte'
    import TopAppBar from './BattleInfo/TopAppBar.svelte'   //Complete

    import BattleInfo from './BattleInfo/BattleInfo.svelte' //Complete
    import BattleMode from './BattleMode/BattleMode.svelte'  //Complete
    import TwoPlayerMode from './Players/TwoPlayerMode.svelte' //Complete
    import RoundSplash from './RoundPage/RoundSplash.svelte' //Complete
    import RoundPage from './RoundPage/RoundPage.svelte'  //Complete
    import RoundResult from './RoundPage/RoundResult.svelte';

    const routes = {
        '/': BattleInfo,
        '/battlemode': BattleMode,
        '/pvp': TwoPlayerMode,
        '/roundsplash': RoundSplash,
        '/battleround': RoundPage,
        '/battleresult': RoundResult
    }

</script>

<body>
    <Router {routes}/>
</body>





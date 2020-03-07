<svelte:head>
    <style>
        .right-player-select{
            clip-path: polygon(0% 0%, 0% 100%, 100% 100%, 100% 65%, 98% 50%, 100% 35%, 100% 0%);
        }
        .left-player-select{
            clip-path: polygon(0% 0%, 0% 35%, 2% 50%, 0% 65%, 0% 100%, 100% 100%, 100% 0%);
        }
        .both-player-select{
            clip-path: polygon(0% 0%, 0% 35%, 2% 50%, 0% 65%, 0% 100%, 100% 100%, 100% 65%, 98% 50%, 100% 35%, 100% 0%);
        }
        .verticalrotate {
          transform: rotate(-90deg);
          transform-origin: right top;
          -ms-transform: rotate(-90deg);
          -ms-transform-origin:right top;
          -webkit-transform: rotate(-90deg);
          -webkit-transform-origin:right top
        }


    </style>
</svelte:head>

<script>
    import { onMount } from 'svelte';
    import Router from 'svelte-spa-router';
    import {link} from 'svelte-spa-router'
    import {push, pop, replace} from 'svelte-spa-router'

    import { Col, Container, Row } from "sveltestrap";
    import { Button } from "sveltestrap";
    import { Progress } from 'sveltestrap';
    import RoundContainer from './RoundContainer.svelte';

    import { players } from '../Store/PlayerStore.js';
    import { battle_quiz } from '../Store/BattleDataStore.js';


    let colors = ["light", "success", "danger", "warning"];

    let questions = $battle_quiz["questions"];

    import {remaining_time, time_progress, round_number } from '../Store/DataStore.js';
    import { opponent_score, my_score} from '../Store/DataStore.js';
    let question = questions[$round_number - 1];
    let candidates = question["candidates"];
    let right_answer = question["answer"];


    let answer_result = 0; // case for checking that who's answer is correct. this is init case.

    let clicked = false;


    let _t_count = 0;
    let interval;
    onMount(() => {
        interval = setInterval(() => {
            _t_count = _t_count + 1;
            if(_t_count == 10){
                _t_count = 0;
                remaining_time.update(n => n - 1);
                if($remaining_time == 0){
                    clearInterval(interval);
                    checkAnswer();
                    return;
                }
            }
            time_progress.update(n => n - 1);

        }, 100);

        return () => {
            clearInterval(interval);
        };
    });
    let check_interval;
    let delta;
    function checkAnswer(answer){

        if(clicked == true){
            return;
        }
        clearInterval(interval);
        clicked = true;

        let random_index = Math.floor(Math.random() * 10000) % 4;

        let opponent_answer = candidates[random_index];


        if(typeof (answer) == "undefined"){
            if(opponent_answer == right_answer){
                answer_result = 2; // this case is that opponent answer is right.
                delta = $remaining_time * 9;
                opponent_score.update(n => n + delta);
            }
            else{
                answer_result = 4; // this is case that two players are all wrong.
            }
            check_interval = setInterval(() => {
                clearInterval(check_interval);
                round_number.update(n => n + 1);
                time_progress.set(100);
                remaining_time.set(10);
                if($round_number == 11){
                    $players["players"][0]["info"]["score"] = $my_score;
                    $players["players"][1]["info"]["score"] = $opponent_score;

                    if($my_score > $opponent_score){
                        $players["players"][0]["info"]["win_counter"] = $players["players"][0]["info"]["win_counter"] + 1;
                    }
                    else if($my_score < $opponent_score){
                        $players["players"][1]["info"]["win_counter"] = $players["players"][1]["info"]["win_counter"] + 1;
                    }
                    round_number.set(1);
                    push('/battleresult');
                }
                else{
                    push('/roundsplash');
                }
            }, 3000);
        }
        else{
            if(answer == right_answer && opponent_answer == right_answer){
                answer_result = 3; //this is case that two players are all right.
                delta = $remaining_time * 9;
                my_score.update(n => n + delta);
                opponent_score.update(n => n + delta);
            }
            else if(answer == right_answer && opponent_answer != right_answer){
                answer_result = 1; //this is case that I am only right.
                delta = $remaining_time * 9;
                my_score.update(n => n + delta);
            }
            else if(answer != right_answer && opponent_answer == right_answer){
                answer_result = 2;
                delta = $remaining_time * 9;
                opponent_score.update(n => n + delta);
            }
            else{
                answer_result = 4;
            }
            check_interval = setInterval(() => {
                clearInterval(check_interval);
                round_number.update(n => n + 1);
                time_progress.set(100);
                remaining_time.set(10);
                if($round_number == 11){
                    $players["players"][0]["info"]["score"] = $my_score;
                    $players["players"][1]["info"]["score"] = $opponent_score;

                    if($my_score > $opponent_score){
                        $players["players"][0]["info"]["win_counter"] = $players["players"][0]["info"]["win_counter"] + 1;
                    }
                    else if($my_score < $opponent_score){
                        $players["players"][1]["info"]["win_counter"] = $players["players"][1]["info"]["win_counter"] + 1;
                    }
                    round_number.set(1);
                    push('/battleresult');
                }
                else{
                    push('/roundsplash');
                }

            }, 3000);
        }

    }

</script>

<RoundContainer>
    <Container>
        <Row style="padding-top: 50px;">
            <Col>
                <h1 style="text-align: center;">
                    {question["question"]}
                </h1>
            </Col>
        </Row>
    </Container>
    <Container>
        {#each candidates as candidate}
            <Row style="margin-bottom: 20px;">
                <Col style="text-align: center;">
                    <Button class="" color={colors[0]} style="width: 80%; height: 60px;" on:click={()=>checkAnswer(candidate)}>
                        {candidate}
                    </Button>
                </Col>
            </Row>
        {/each}
    </Container>
</RoundContainer>




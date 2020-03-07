<svelte:head>
  <style>
    .top-app-bar-container, .top-app-bar-iframe {
      max-width: 480px;
      min-width: 480px;
      border: 1px solid rgba(0,0,0,.1);
      margin: 0 18px 18px 0;
    }
    .top-app-bar-container {
      overflow: auto;
      display: contents;
    }
    .flexy {
      display: flex;
      flex-wrap: wrap;
    }
    .flexor {
      display: inline-flex;
      flex-direction: column;
    }
    .flexor-content {
      flex-basis: 0;
      height: 0;
      flex-grow: 1;
      overflow: auto;
    }
    .mdc-top-app-bar__title {
      padding: 0;
    }
  </style>
</svelte:head>
<section>
    <div class="flexy">
        <div class="top-app-bar-container">
            <TopAppBar variant="static" {prominent} {dense} color={secondaryColor ? 'primary' : 'secondary'}>
                <Row>
                    <Section>
                        <IconButton class="material-icons">spa</IconButton>
                    </Section>
                    <Section>
                        <Title style="margin: auto;">Basic Math</Title>
                    </Section>
                    <Section class="mdc-top-app-bar__section--align-end">
                        <IconButton class="material-icons">spa</IconButton>
                    </Section>
                </Row>
            </TopAppBar>
        </div>
    </div>
</section>

<script>
  import TopAppBar, {Row, Section, Title} from '@smui/top-app-bar/bare.js';
  import '@smui/top-app-bar/bare.css';
  import IconButton from '@smui/icon-button/bare.js';
  import '@smui/icon-button/bare.css';
  import Checkbox from '@smui/checkbox/bare.js';
  import '@smui/checkbox/bare.css';
  import FormField from '@smui/form-field/bare.js';
  import '@smui/form-field/bare.css';
  let prominent = false;
  let dense = false;
  let secondaryColor = false;
</script>


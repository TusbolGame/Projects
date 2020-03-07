<svelt:head>
    <style>
        .switch-panel{
            color: #fff;
            background-color: #fff;
            padding-bottom: 40px;
        }
        .tab-content{
            width: 90%;
        }

        .tab-text{
            color:#00B5D7 !important;
            font-size: 1.3em;
            font-weight: bold;
        }
        .mdc-tab--active>span>span.tab-text{
            color: #fff !important;
        }
        .mdc-tab{
            background-color: #fff !important;
            width: 50%;
        }
        .mdc-tab:nth-child(1){
            border-right: 1px solid #00B5D7;
        }
        .mdc-tab:nth-child(2){
            border-left: 1px solid #00B5D7;
        }
        .mdc-tab--active{
            background-color: #00B5D7 !important;
        }
        .mdc-tab-scroller{
            width: 90% !important;
            margin:auto;
            border-radius: 25px;
            border: 2px solid #00B5D7;
        }

        .mdc-tab-indicator .mdc-tab-indicator__content--underline {
            display:none;
        }

    </style>
</svelt:head>
<section>
  <div class="switch-panel">
    <h5 style="color: #646464; text-align: center; padding: 10px 0px">
        Sort feed
    </h5>
    <TabBar tabs={['BEST', 'NEWEST',]} let:tab bind:active>
      <Tab {tab}>
        <Label class="tab-text">{tab}</Label>
      </Tab>
    </TabBar>
    <!--
    <pre class="status">Selected: {active}</pre>
    -->
  </div>
</section>

<script>
  import Tab, {Icon, Label} from '@smui/tab/bare.js';
  import '@smui/tab/bare.css';
  import TabBar from '@smui/tab-bar/bare.js';
  import '@smui/tab-bar/bare.css';
  import Button from '@smui/button/bare.js';
  import '@smui/button/bare.css';
  let active = 'Home';
  let iconTabs = [
    {
      icon: 'access_time',
      label: 'Recents'
    },
    {
      icon: 'near_me',
      label: 'Nearby'
    },
    {
      icon: 'favorite',
      label: 'Favorites'
    }
  ];
  let keyedTabs = [
    {
      k: 1,
      icon: 'code',
      label: 'Code'
    },
    {
      k: 2,
      icon: 'code',
      label: 'Code'
    },
    {
      k: 3,
      icon: 'code',
      label: 'Code'
    },
    {
      k: 4,
      icon: 'code',
      label: 'Code'
    }
  ];
  let keyedTabsActive = keyedTabs[2];
</script>

<style>
  section > div {
    margin-bottom: 40px;
  }
  .icon-indicators :global(.mdc-tab-indicator--active .mdc-tab-indicator__content) {
    opacity: .2;
  }
</style>
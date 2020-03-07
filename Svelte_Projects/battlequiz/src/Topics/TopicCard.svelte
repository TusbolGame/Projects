<svelte:head>
  <title>Cards - SMUI</title>
  <style>
    .card-container {
      display: inline-flex;
      justify-content: center;
      align-items: center;
      min-height: 500px;
      min-width: 380px;
      background-color: #f8f8f8;
      margin-right: 20px;
      margin-bottom: 20px;
    }
    .card-container.short {
      min-height: 200px;
    }
    .card-media-16x9{
      background-image: url(https://via.placeholder.com/320x180.png?text=16x9);
    }
    * :global(.card-media-16x9) {
      background-image: url(https://via.placeholder.com/320x180.png?text=16x9);
    }
    * :global(.card-media-square) {
      background-image: url(https://via.placeholder.com/320x320.png?text=square);
    }
  </style>
</svelte:head>

<script>
  import Card, {Content, PrimaryAction, Media, MediaContent, Actions, ActionButtons, ActionIcons} from '@smui/card/bare';
  import '@smui/card/bare.css';
  import Button, {Label} from '@smui/button/bare';
  import '@smui/button/bare.css';
  import IconButton, {Icon} from '@smui/icon-button/bare';
  import '@smui/icon-button/bare.css';
  import List, {Item, Text} from '@smui/list/bare';
  import '@smui/list/bare.css'
</script>

<section>
  <div style="display: flex; flex-wrap: wrap;">
    <Card style="width: 320px;">
        <Media class="card-media-16x9" aspectRatio="16x9">
          <MediaContent>
            <h2 class="mdc-typography--headline6" style="color: #fff; position: absolute; bottom: 16px; left: 16px; margin: 0;">A card with 16x9 media.</h2>
          </MediaContent>
        </Media>
        <Content style="color: #888;">
          Here's some gray text down here.
        </Content>
    </Card>
  </div>
</section>



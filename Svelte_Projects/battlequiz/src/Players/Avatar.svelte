<svelte:head>
    <style>
        img {
            width: 100%;
            height: auto;
            border-radius: 50%;
        }
        .avatar {
            position: relative;
            display: inline-block;
            margin-bottom: 1rem;
            margin-top: 1rem;
        }
    </style>
</svelte:head>

<script>
    export let url = ''
    export let value = 0
    const u = Math.PI * 125 * 2 - 0.1
    $: offset = u * value
    $: style = `stroke-dashoffset: ${offset}px; stroke-dasharray: ${u}px; opacity: ${1-value}`
</script>

<div class="avatar">
    <img src={url} alt="image of">
</div>
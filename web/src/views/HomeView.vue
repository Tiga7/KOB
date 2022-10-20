<template>
    <div>
        <CardView>
            home view
        </CardView>
        <img class="snake" src="@/assets/image/695e3.jpeg" />
    </div>
</template>

<script>
import CardView from '@/components/CardView.vue';
export default {
    components: { CardView }
}
</script>

<style scoped>
img.snake {
    margin: 20px auto;
    display: flex;

    justify-content: center;
    align-items: center;
}
</style>
<template>
    <div>
        <CardView>
            <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <th scope="col">排名</th>
                        <th scope="col">用户名</th>
                        <th scope="col">积分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in users" :key="item.id">
                        <td>{{ index + 1 }}</td>
                        <td>
                            <span>{{ item.username }}</span>
                        </td>
                        <td>{{ item.rating }}</td>

                    </tr>
                </tbody>
            </table>
            <nav aria-label="Page navigation example" style="float: right;">
                <ul class="pagination">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">前一页</a>
                    </li>
                    <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number"
                        @click="click_page(page.number)">
                        <a class="page-link" href="#">{{ page.number }}</a>
                    </li>
                    <li class="page-item" @click="click_page(-1)">
                        <a class="page-link" href="#">后一页</a>
                    </li>
                </ul>
            </nav>
        </CardView>
    </div>
</template>

<script>
import CardView from '@/components/CardView.vue';

import { useStore } from 'vuex'
import { ref } from 'vue';
import $ from 'jquery'
export default {
    components: { CardView },

    setup() {

        const store = useStore();
        let users = ref([]);
        let current_page = 1;
        let total_count = 0;
        //分页器里展示的项
        let pages = ref([]);
        let page_size = 10;

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_count / page_size));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i++) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : ""
                    });
                }
            }
            pages.value = new_pages;
        }

        const click_page = (page) => {
            if (page === -2) page = current_page - 1;
            if (page === -1) page = current_page + 1;
            let max_page = parseInt(Math.ceil(total_count / page_size));
            if (1 <= page && page <= max_page) {
                current_page = page;
                get_list(current_page);
                update_pages();
            }
        }
        const get_list = (page) => {
            current_page = page;
            $.ajax({
                url: "http://localhost:3030/rank/ranklist/",
                type: "get",
                data: {
                    page
                },
                headers: {
                    authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    console.log(resp);
                    users.value = resp.users;
                    total_count = resp.total_count;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }
        get_list(current_page);

        return {
            users,
            total_count,
            pages,
            get_list,
            click_page
        }
    }
}
</script>

<style scoped>
img.user-photo {
    width: 6vh;
    border-radius: 50%;
}
</style>
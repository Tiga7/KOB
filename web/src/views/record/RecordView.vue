<template>
    <div>
        <CardView>
            <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <th scope="col">序号</th>
                        <th scope="col">A</th>
                        <th scope="col">B</th>
                        <th scope="col">对战结果</th>
                        <th scope="col">对战时间</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in records" :key="item.record.id">
                        <td>{{ index + 1 }}</td>
                        <td>
                            <img :src="item.a_photo" class="user-photo">
                            &nbsp;
                            <span class="user-name">{{ item.a_username }}</span>
                        </td>
                        <td>
                            <img :src="item.b_photo" class="user-photo">
                            &nbsp;
                            <span class="user-name">{{ item.b_username }}</span>
                        </td>
                        <td>{{ item.result }}</td>
                        <td>{{ item.record.createTime }}</td>
                        <td>
                            <button type="button" class="btn btn-primary"
                                @click="open_record_steps(item.record.id)">观看录像</button>
                        </td>
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
import router from '@/router';
import $ from 'jquery'
export default {
    components: { CardView },

    setup() {

        const store = useStore();
        let records = ref([]);
        let current_page = 1;
        let total_count = 0;
        //分页器里展示的项
        let pages = ref([]);

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_count / 10));
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

            let max_page = parseInt(Math.ceil(total_count / 10));

            if (1 <= page && page <= max_page) {
                current_page = page;
                get_list(current_page);
                update_pages();
            }

        }
        const get_list = (page) => {
            current_page = page;
            $.ajax({
                url: "http://localhost:3030/record/getlist/",
                type: "get",
                data: {
                    page
                },
                headers: {
                    authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.data;
                    total_count = resp.total_count;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }
        get_list(current_page);

        const stringTo2D = (map) => {

            let g = [];
            for (let i = 0, k = 0; i < 13; i++) {
                let line = [];
                for (let j = 0; j < 14; j++, k++) {
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }
        const open_record_steps = (record_id) => {
            for (const record of records.value) {
                if (record.record.id === record_id) {
                    console.log(record);

                    store.commit("updateIsRecord", true);

                    store.commit("updateGame", {
                        map: stringTo2D(record.record.map),
                        a_id: record.aid,
                        a_sx: record.asx,
                        a_sy: record.asy,
                        b_id: record.bid,
                        b_sx: record.bsx,
                        b_sy: record.bsy,
                    })

                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    })
                    //跳转到某个页面
                    router.push({
                        name: "record_steps",
                        //参数
                        params: {
                            recordId: record_id,
                        }
                    })
                    break;
                }
            }
        }


        return {
            records,
            total_count,
            pages,
            get_list,
            open_record_steps,
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
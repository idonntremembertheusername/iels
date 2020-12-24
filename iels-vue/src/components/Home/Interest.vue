<template>
  <div class="interest-box" v-if="showBox">
    <div class="mask"></div>
    <div class="interest-cont" style="text-align:center;">
      <h2 class="main-category-tt">
        设置学习兴趣
        <!--        <span class="main-category-subtitle">-->
        <!--          已选择-->
        <!--          <i class="js-select-item-num">3</i>-->
        <!--          /6个学院-->
        <!--        </span>-->
      </h2>
      <ul class="main-category-list">
        <li class="main-category-item">
          <div class="main-category-hover">

            <ul class="small-category">
              <li
                class="small-category-item"
                v-for="(item,index) in categoryList"
                :key="index"
                :class="{active:item.active}"
                @click="clickActive(item,index)"
              >{{item.name}}
              </li>
            </ul>
          </div>
        </li>
        <div style="clear:both"></div>
      </ul>
      <div style="clear:both"></div>
      <div class="main-category-btngroup">
        <span class="btn-outline main-category-cancel" @click="concealHandler()">下次再选</span>
        <span class="btn-default main-category-save" @click="saveUserInterest">保存</span>
      </div>
    </div>
  </div>
</template>
<script>
    export default {
        name: "InterestBox",
        data() {
            return {
                categoryList: [],
                selectedCategory:{
                    userid:'',
                    categoryid:''

                }

            };
        },
        methods: {
            concealHandler() {
                this.$store.dispatch("conceal_box");
            },
            clickActive(item, index) {
                // console.log(item)
                this.selectedCategory.categoryid = item.id
                if (item.active) {
                    this.$set(item, 'active', false)``
                } else {
                    this.$set(item, 'active', true)
                }
            },
            saveUserInterest(){
                var qs=require('qs')
                const {data: res} =  this.$http.post('/ucenter/saveUserInterest', qs.stringify(this.selectedCategory))
                // if (!res.success) {
                //     if (res.message) {
                //         // return this.$message.error(res.message)
                //     } else {
                //         this.$message.error('修改失败')
                //     }
                // }
                this.$message.success('操作成功')
                console.log(res)


            },
            //课程分类
            getCategoryList() {
                this.$https.categoryList('/course/category/list').then(res => {
                    this.categoryList = res.children;
                });
            },
        },
        mounted() {
            this.getCategoryList();
            this.showInterestBox = this.$store.state.showInterestBox;
        },
        computed: {
            showBox: function () {
                return this.$store.state.showInterestBox
            }
        },
    };
</script>
<style scoped>
  .main-category-tt {
    font-size: 32px;
    height: 45px;
    line-height: 45px;
    color: #000;
    margin-top: 36px;
    text-align: left;
  }

  h2 {
    font-weight: 400;
  }

  .main-category-subtitle {
    display: inline-block;
    vertical-align: middle;
    font-size: 16px;
    color: #999;
  }

  .main-category-subtitle i {
    color: #23b8ff;
  }

  .main-category-list {
    width: 495px;
    margin: 12px 0 0 -90px;
  }

  .main-category-item {
    width: 495px;
    float: left;
    text-align: left;
    margin-top: 24px;
    margin-left: 90px;
    color: #000;
  }

  .main-category-item h3 {
    font-size: 12px;
    color: #777;
    line-height: 16px;
    font-weight: 400;
  }

  .small-category {
    margin-top: 2px;
  }

  .small-category-item {
    float: left;
    padding: 7px 10px;
    border: 0.5px solid;
    border-color: #bbb;
    border-radius: 80px;
    cursor: pointer;
    margin: 10px 6px 0 0;
    line-height: 14px;
  }

  .main-category-item .small-category-item.active {
    background: rgba(35, 184, 255, 0.1);
    color: #23b8ff;
    border-color: #23b8ff;
  }

  .main-category-btngroup {
    position: absolute;
    right: 36px;
    bottom: 24px;
  }

  .main-category-cancel {
    color: #333;
    border-color: #bbb;
    border-width: 1px;
    line-height: 48px;
  }

  .main-category-save {
    line-height: 50px;
    margin-left: 16px;
    background-color: #23b8ff;
    color: #fff;
  }

  .main-category-save,
  .main-category-cancel {
    margin-top: 25px;
    width: 140px;
    font-size: 18px;
  }

  .btn-outline {
    color: #23b8ff;
    background-color: #fff;
  }

  .btn-default,
  .btn-primary,
  .btn-weak,
  .btn-outline,
  .btn-card {
    display: inline-block;
    vertical-align: middle;
    cursor: pointer;
    font-size: 14px;
    text-align: center;
    border-radius: 2px;
    border: 1px solid #23b8ff;
    user-select: none;
  }

  .interest-box {
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0px;
    top: 0px;
    z-index: 999;
  }

  .interest-box .mask {
    background: rgba(0, 0, 0, 0.4);
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0px;
    top: 0px;
    z-index: 999;
  }

  .interest-box .interest-cont {
    position: fixed;
    width: 70%;
    height: 490px;
    top: 50%;
    left: 50%;
    padding: 35px;
    transform: translate(-50%, -50%);
    background: url("https://8.url.cn/edu/modules/indexNew/img/select-bg.af48313a.png");
    z-index: 999;
  }

  .interest-box .interest-cont .top-title {
    color: #00a4ff;
    font-size: 22px;
  }

  .interest-box .interest-cont .top-title span {
    color: #787d82;
    font-size: 14px;
  }

  .interest-box .interest-cont .item-box {
    overflow: hidden;
  }

  .modal-open {
    overflow: hidden;
  }

  .modal-open div:nth-child(1) {
    margin-left: 0;
  }

  .interest-box .interest-cont .item-box .item {
    float: left;
    width: 30%;
    margin: 2% 0 1% 2%;
  }

  .modal-open div:last-child {
    margin-right: 0;
  }

  .interest-box .interest-cont .item-box .item .title {
    font-size: 16px;
  }

  .interest-box .interest-cont .item-box .item li {
    padding: 5px 20px;
    margin: 10px 5px;
    list-style: none;
    float: left;
    border: solid 1px #787d82;
  }

  .interest-box .button {
    text-align: center;
    padding: 10px;
    font-size: 22px;
  }

  .interest-box .button span {
    cursor: pointer;
    width: 140px;
    margin: 0 10px;
    display: inline-block;
    padding: 5px;
    color: #00a4ff;
    border: 1px solid #00a4ff;
  }

  .interest-box .button span:nth-child(1) {
    color: #fff;
    background: #00a4ff;
    border: 1px solid #00a4ff;
  }
</style>

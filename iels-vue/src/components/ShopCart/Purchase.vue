<template>
  <div class="purchase-wrap" style="font-family:PingFangSC-Regular">
    <ul class="purchase-head-wrap">
      <li class="purchase-item1">课程</li>
      <li class="purchase-item2">有效期</li>
      <li class="purchase-item3">价格</li>
    </ul>
    <div class="order-detail-list">
      <div class="project-list">
        <div class="project">
          <div class="module">
            <div class="info">
              <img
                src="https://hcdn1.luffycity.com/static/frontend/course/160/jquery5@2x_1573035082.7622592.png"
                alt
                width="170"
                height="auto"
              />
              <p>
                <span class="product-fonts name">6小时玩转Jquery核心项目实战</span>
              </p>
            </div>
            <div class="validPeriod">永久有效</div>
            <div class="product-fonts coursePrice">
              <span class="price">￥ 9.90</span>
            </div>
          </div>
        </div>
      </div>
      <ul class="pad-left">
        <div class="left">
          <span>支付方式：</span>
          <div class="paytype">
            <li class="payment">
              <img
                src="https://hcdn1.luffycity.com/static/frontend/activity/zfb_1564141046.0915222.svg"
                alt
              />
              <span>支付宝</span>
            </li>
          </div>
        </div>
        <li class="pay">
          <span class="bot-font-small">课程价格：
              <span class="true-price">￥ 9.90</span>
          </span>
          <el-button type="primary" round>立即支付</el-button>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
  export default {
    name: "Purchase"
  };
</script>
<style scoped>
  .purchase-wrap {
    width: 1200px;
    margin: 0 auto;
  }
  .purchase-head-wrap {
    width: 100%;
    height: 70px;
    color: #333;
    font-size: 16px;
    margin-top: 40px;
    background: #f3f3f3;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-align: center;
    align-items: center;
    position: relative;
    font-family: PingFangSC-Medium;
  }
  .purchase-item1 {
    width: 800px;
    text-align: left;
    margin-left: 30px;
    font-family: PingFangSC-Regular;
  }
  .purchase-item2 {
    width: 80px;
    text-align: center;
    font-family: PingFangSC-Regular;
  }
  .purchase-item3 {
    flex: 1;
    text-align: right;
    margin-right: 45px;
    font-family: PingFangSC-Regular;
  }
  .project {
    padding: 24px 0;
    border-bottom: 1px solid #e8e8e8;
  }
  .pad-left .left,
  .pad-left {
    display: flex;
    align-items: center;
  }
  .pad-left .left {
    margin-left: 30px;
  }
  .pad-left {
    margin-top: 30px;
    width: 1200px;
    margin-bottom: 180px;
    justify-content: space-between;
    position: relative;
  }
  .pay {
    float: right;
    display: flex;
    align-items: center;
    margin-right: 45px;
  }
  .pad-left .left .payment,
  .pad-left .left .paytype {
    display: flex;
    align-items: center;
  }
  .pad-left .left .payment {
    width: 120px;
    height: 48px;
    border-radius: 2.64px;
    margin-right: 36px;
    border: 1px solid #e9e9e9;
    -ms-flex-pack: center;
    justify-content: center;
    font-size: 18px;
    cursor: pointer;
    position: relative;
  }
  .pad-left .left .payment img {
    width: 26px;
    height: 26px;
    margin-right: 8px;
  }
  .project .module .info,
  .project .module {
    display: flex;
    align-items: center;
  }
  .project .module {
    width: 100%;
    height: 100%;
    position: relative;
  }
  .project .module .info {
    width: 802px;
    margin-left: 30px;
    font-family: PingFangSC-Regular;
  }
  .project .module .info img {
    margin-right: 24px;
  }
  .project .module .info span {
    display: block;
  }
  .project .module .info .name {
    font-size: 16px;
    color: #333;
    letter-spacing: 0.36px;
  }
  .project .module .validPeriod {
    width: 80px;
    text-align: center;
  }
  .project .module .coursePrice {
    flex: 1;
    text-align: right;
    margin-right: 45px;
  }
  .project .module .coursePrice span {
    display: block;
    font-size: 18px;
  }
  .project .module .coursePrice .price {
    color: #333;
    margin-bottom: 10px;
    font-family: PingFangSC-Regular;
  }
  .product-fonts {
    font-family: PingFangSC-Regular;
    font-size: 18px;
    color: #333;
    letter-spacing: 0.36px;
  }
  .bot-font-small {
    margin-right: 15px;
  }
  .true-price{
    font-size: 36px;
    color: #333;
  }
  .go-buy-btn {
    width: 110px;
    height: 38px;
    outline: none;
    border: none;
    color: #fff;
    line-height: 38px;
    background: #ffc210;
    border-radius: 4px;
    font-family: PingFangSC-Medium;
    font-size: 16px;
  }
</style>

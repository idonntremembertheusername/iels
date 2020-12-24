<template>
  <div id="app">
    <Header/>
    <router-view/>
    <Footer/>

  </div>
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {
        path: ''
      }
    },
  }
</script>

<style>

</style>

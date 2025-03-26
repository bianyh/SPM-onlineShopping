<template>
  <el-dropdown trigger="click" @command="handleShopClick">
    <a class="dropdown-trigger">
      Shops <el-icon><arrow-down /></el-icon>
    </a>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="/seller-home">My Shop</el-dropdown-item>
        <el-dropdown-item command="/shop-application">Application</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import { ElIcon } from 'element-plus';
import { ArrowDown } from '@element-plus/icons-vue';

const router = useRouter();
const dropdownRef = ref(null);

/*onMounted(() => {
  console.log('Dropdown element:', dropdownRef.value); 
  if (dropdownRef.value) {
      const rect = dropdownRef.value.$el.getBoundingClientRect(); 
      console.log('Dropdown rect:', rect);
  }
});*/

const handleShopClick = (command) => {
  router.push(command);
};
</script>

<style scoped>
/* 让 Shops 选项与 Categories 保持一致 */
.dropdown-trigger {
  align-items: center;
  font-size: 22px;
}

/* 调整下拉菜单的样式，使其与 Categories 相匹配 */
el-dropdown-menu {
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  box-shadow: 5px 5px #00000040;
}

el-dropdown-item {
  color: rgb(70, 70, 70);
}

el-dropdown-item:hover {
  background-color: rgba(0, 0, 0, 0.24);
}
</style>


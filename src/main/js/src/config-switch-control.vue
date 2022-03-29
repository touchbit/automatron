<!--
  - Copyright 2014-2019 the original author or authors.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="field is-grouped config-switch-control">
    <div class="control buttons has-addons">
      <button
          v-for="switchOption in [true, false]"
          :key="switchOption"
          class="button config-switch-control__level"
          :class="cssClass(switchOption)"
          v-text="getLabel(switchOption)"
          @click.stop="select(switchOption)"
      />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Boolean,
      required: true
    },
    status: {
      type: Object,
      default: null
    },
  },
  methods: {
    select(value) {
      this.$emit('input', value);
    },
    cssClass(switchOption) {
      if (this.value !== switchOption) {
        return 'config-switch-control__level--inherited';
      }
      if (this.value && switchOption) {
        return 'is-active is-success';
      }
      return 'is-active is-danger';
    },
    getLabel(value) {
      return value ? 'Enable' : 'Disable';
    },
  }
}
</script>

<style lang="scss">
.config-switch-control {
  &__level {
    &--inherited {
      opacity: 0.5;

      &:hover {
        opacity: 1;
      }
    }
  }
}
</style>

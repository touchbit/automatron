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
          v-for="switchOption in this.configMap.parameterValues"
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
    configMap: {
      type: Object,
      parameterName: String,
      parameterValue: String,
      parameterType: String,
      parameterValues: {
        type: Array,
      }
    }
  },
  methods: {
    select(confValue) {
      console.info(' >>>>>>> confValue >>>>>>>>>> '.concat(confValue))
      this.configMap.parameterValue = confValue
      console.info(' >>>>>>>> configMap >>>>>>>>> '.concat(JSON.stringify(this.configMap)))
      this.$emit('input', this.configMap);
    },
    cssClass(switchOption) {
      if (this.configMap.parameterName === 'DEFAULT_LANGUAGE') {
        if (switchOption !== this.configMap.parameterValue) {
          return 'config-switch-control__level--inherited';
        } else {
          return 'is-active is-success';
        }
      } else {
        let booleanSwitchOption = (switchOption === 'true');
        let booleanParameterValue = (this.configMap.parameterValue === 'true');
        if (booleanParameterValue !== booleanSwitchOption) {
          return 'config-switch-control__level--inherited';
        }
        if (booleanParameterValue && booleanSwitchOption) {
          return 'is-active is-success';
        }
        return 'is-active is-danger';
      }
    },
    getLabel(switchOption) {
      if (this.configMap.parameterName === 'DEFAULT_LANGUAGE') {
        return (switchOption === 'RU') ? 'Russian' : 'English';
      }
      let isTrue = (switchOption === 'true');
      return isTrue ? 'Enable' : 'Disable';
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

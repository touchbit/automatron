<!--
  - Copyright (c) 2022 Shaburov Oleg
  -
  - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  - EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
  - OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
  - IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
  - ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
  - TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
  - SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
      this.configMap.parameterValue = confValue
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

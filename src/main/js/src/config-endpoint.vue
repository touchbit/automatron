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
  <div class="config">
    <section class="section">
      <div class="details-header">
        <h1 v-if="instance" class="title">Automatron configuration</h1>
      </div>
      <table class="table is-hoverable is-fullwidth">
        <thead>
        <tr>
          <th>
            Global configuration
          </th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>
            <span class="is-breakable">Default language</span>&nbsp;
            <config-switch-control :configMap="configs.DEFAULT_LANGUAGE"
                                   class="is-pulled-right"
                                   @input="value => this.patchConfig(value)"
            />
          </td>
        </tr>
        </tbody>
      </table>
      <table class="table is-hoverable is-fullwidth">
        <thead>
        <tr>
          <th>
            Swagger UI configuration
          </th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>
            <span class="is-breakable">Display default request header: 'Language'</span>&nbsp;
            <config-switch-control :configMap="configs.ENABLE_DEFAULT_LOCALE_HEADER"
                                   class="is-pulled-right"
                                   @input="value => this.patchConfig(value)"
            />
          </td>
        </tr>
        <tr>
          <td>
            <span class="is-breakable">Display default request header: 'Request-ID'</span>&nbsp;
            <config-switch-control :configMap="configs.ENABLE_DEFAULT_REQUEST_ID_HEADER"
                                   class="is-pulled-right"
                                   @input="value => this.patchConfig(value)"
            />
          </td>
        </tr>
        <tr>
          <td>
            <span class="is-breakable">Display default response group: '5xx'</span>&nbsp;
            <config-switch-control :configMap="configs.ENABLE_DEFAULT_5_XX_RESPONSE"
                                   class="is-pulled-right"
                                   @input="value => this.patchConfig(value)"
            />
          </td>
        </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script>
import ConfigSwitchControl from './config-switch-control'

export default {
  components: {ConfigSwitchControl},
  props: {
    instance: { //<1>
      type: Object,
      required: true
    }
  },
  data: () => ({
    configs: {
      ENABLE_DEFAULT_5_XX_RESPONSE: Object,
      ENABLE_DEFAULT_LOCALE_HEADER: Object,
      ENABLE_DEFAULT_REQUEST_ID_HEADER: Object,
      DEFAULT_LANGUAGE: Object,
    }
  }),
  async created() {
    try {
      let response = await this.instance.axios.get('actuator/config');
      this.configs = response.data;
    } catch (error) {
      console.warn('Fetching configuration failed:', error);
    }
  },
  methods: {
    async patchConfig(body) {
      try {
        let response = await this.instance.axios.patch('actuator/config', body);
        this.configs = response.data;
      } catch (error) {
        console.warn('Configure failed:', error);
      }
    },
  },
};
</script>

<style lang="scss">
@import "./assets/css/utilities";

.details-header {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;

  &__urls {
    width: 100%;
    text-align: center;
  }
}

.config {
  &__header {
    background-color: $white;
    z-index: 10;
    padding: 0.5em 1em;
  }

  &__toggle-scope {
    width: 10em;
  }
}
</style>

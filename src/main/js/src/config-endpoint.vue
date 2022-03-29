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
            Swagger UI configuration
          </th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>
            <span class="is-breakable">Display default 'Locale' header</span>&nbsp;
            <config-switch-control class="is-pulled-right"
                                   :value="config.openapi.enableDefaultLocaleHeader"
                                   @input="value => this.updated(function () {
                                     config.openapi.enableDefaultLocaleHeader = value;
                                     return config;
                                   })"
            />
          </td>
        </tr>
        <tr>
          <td>
            <span class="is-breakable">Display default 'Request-ID' header</span>&nbsp;
            <config-switch-control class="is-pulled-right"
                                   :value="config.openapi.enableDefaultRequestIdHeader"
                                   @input="value => this.updated(function () {
                                     config.openapi.enableDefaultRequestIdHeader = value;
                                     return config;
                                   })"
            />
          </td>
        </tr>
        <tr>
          <td>
            <span class="is-breakable">Display default '5xx' response</span>&nbsp;
            <config-switch-control class="is-pulled-right"
                                   :value="config.openapi.enableDefault5xxResponse"
                                   @input="value => this.updated(function () {
                                     config.openapi.enableDefault5xxResponse = value;
                                     return config;
                                   })"
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
    config: {
      openapi: {
        enableDefaultLocaleHeader: Boolean,
        enableDefaultRequestIdHeader: Boolean,
        enableDefault5xxResponse: Boolean,
      }
    }
  }),
  async created() {
    try {
      const response = await this.instance.axios.get('actuator/config');
      this.config = response.data;
    } catch (error) {
      console.warn('Fetching configuration failed:', error);
    }
  },
  methods: {
    async updated(func) {
      try {
        const body = func.call()
        const response = await this.instance.axios.post('actuator/config', body);
        this.config = response.data;
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

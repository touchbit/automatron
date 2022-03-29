/*
 * Copyright (c) 2022 Shaburov Oleg
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global SBA */
import config from './config';
import configEndpoint from './config-endpoint';

// tag::customization-ui-toplevel[]
SBA.use({
  install({viewRegistry}) {
    viewRegistry.addView({
      name: 'config',  //<1>
      path: '/config', //<2>
      component: config, //<3>
      label: 'Config', //<4>
      order: 1000, //<5>
    });
  }
});
// end::customization-ui-toplevel[]

// tag::customization-ui-endpoint[]
SBA.use({
  install({viewRegistry, vueI18n}) {
    viewRegistry.addView({
      name: 'instances/config',
      parent: 'instances', // <1>
      path: 'config',
      component: configEndpoint,
      label: 'Config',
      group: 'config', // <2>
      order: 1000,
      isEnabled: ({instance}) => instance.hasEndpoint('config') // <3>
    });

    vueI18n.mergeLocaleMessage('en', { // <4>
      sidebar: {
        custom : {
          title : "Config Extension"
        }
      }
    });
  }
});
// end::customization-ui-endpoint[]

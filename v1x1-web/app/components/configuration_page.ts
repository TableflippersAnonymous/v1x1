import {Component} from '@angular/core';
import {V1x1Module} from "../model/v1x1_module";
import {V1x1ConfigurationDefinitionSet} from "../model/v1x1_configuration_definition_set";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {Permission, V1x1ConfigurationDefinitionField, ConfigType} from "../model/v1x1_configuration_definition_field";

@Component({
  selector: 'configuration-page',
  template: `<ngb-tabset class="tabs-left">
    <ngb-tab *ngFor="let v1x1Module of v1x1Modules" [title]="v1x1Module.displayName">
      <template ngbTabContent>
        <configuration-module [v1x1Module]="v1x1Module"></configuration-module>
      </template>
    </ngb-tab>
  </ngb-tabset>`
})
export class ConfigurationPageComponent {
  /* This will eventually be pulled from the API. */
  v1x1Modules = [
    new V1x1Module("echo", "Echo", "This is a module used for testing.  It announces many events in the channel.", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.NONE,
        [new V1x1ConfigurationDefinitionField(
          "Enabled?",
          "This controls whether this module is enabled on your channel.",
          "null",
          ConfigType.MASTER_ENABLE,
          [],
          Permission.READ_WRITE,
          "enabled",
          null
        )],
        {}
      ),
      null
    )),
    new V1x1Module("i18n_test", "I18n Test", "This module allows testing the internationalization code.", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.NONE,
        [new V1x1ConfigurationDefinitionField(
          "Enabled?",
          "This controls whether this module is enabled on your channel.",
          "null",
          ConfigType.MASTER_ENABLE,
          [],
          Permission.READ_WRITE,
          "enabled",
          null
        )],
        {}
      ),
      null
    )),
    new V1x1Module("timed_messages", "Timers", "This module controls repeating messages to a channel -- timers, if you will", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.READ_WRITE,
        [
          new V1x1ConfigurationDefinitionField(
            "Enabled?",
            "This controls whether this module is enabled on your channel.",
            "null",
            ConfigType.MASTER_ENABLE,
            [],
            Permission.READ_WRITE,
            "enabled",
            null
          ),
          new V1x1ConfigurationDefinitionField(
            "Timers",
            "List of rotations you have configured",
            "null",
            ConfigType.COMPLEX_STRING_MAP,
            [],
            Permission.READ_WRITE,
            "timers",
            "tv.v1x1.modules.channel.timed_messages.Timer"
          )
        ],
        {
          "tv.v1x1.modules.channel.timed_messages.TimerEntry": [
            new V1x1ConfigurationDefinitionField(
              "Message",
              "<no description>",
              "null",
              ConfigType.STRING,
              [],
              Permission.READ_WRITE,
              "message",
              null
            )
          ],
          "tv.v1x1.modules.channel.timed_messages.Timer":[
            new V1x1ConfigurationDefinitionField(
              "Timer Entries",
              "The list of messages this rotation will feature",
              "null",
              ConfigType.COMPLEX_LIST,
              [],
              Permission.READ_WRITE,
              "entries",
              "tv.v1x1.modules.channel.timed_messages.TimerEntry"
            ),
            new V1x1ConfigurationDefinitionField(
              "Interval",
              "Time (in milliseconds) between messages",
              "null",
              ConfigType.STRING,
              [],
              Permission.READ_WRITE,
              "interval",
              null
            ),
            new V1x1ConfigurationDefinitionField(
              "Enabled?",
              "Whether or not this rotation will run",
              "null",
              ConfigType.STRING,
              [],
              Permission.READ_WRITE,
              "enabled",
              null
            ),
            new V1x1ConfigurationDefinitionField(
              "Always On",
              "Should this rotation run while you're offline?",
              "null",
              ConfigType.BOOLEAN,
              [],
              Permission.READ_WRITE,
              "alwayson",
              null
            ),
            new V1x1ConfigurationDefinitionField(
              "activeTimer",
              "<no description>",
              "null",
              ConfigType.STRING,
              [],
              Permission.NONE,
              "active_uuid",
              null
            )
          ]
        }
      ),
      null
    )),
    new V1x1Module("caster", "Caster Command", "Enables the CASTER/FOLLOW command", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.READ_WRITE,
        [new V1x1ConfigurationDefinitionField(
          "Enabled?",
          "This controls whether this module is enabled on your channel.",
          "null",
          ConfigType.MASTER_ENABLE,
          [],
          Permission.READ_WRITE,
          "enabled",
          null
        )],
        {}
      ),
      null
    )),
    new V1x1Module("tmi", "Twitch", "This module controls settings used when connecting to your Twitch Channel.", new V1x1ConfigurationDefinitionSet(
      new V1x1ConfigurationDefinition(
        Permission.READ_WRITE,
        [
          new V1x1ConfigurationDefinitionField(
            "Global Bots:",
            "These are the valid global bots and their OAuth tokens",
            "null",
            ConfigType.STRING_MAP,
            [],
            Permission.READ_WRITE,
            "global_bots",
            null
          ),
          new V1x1ConfigurationDefinitionField(
            "Connections per Channel:",
            "This is how many connections to TMI per channel are desired.",
            "3",
            ConfigType.INTEGER,
            [],
            Permission.READ_WRITE,
            "connections_per_channel",
            null
          ),
          new V1x1ConfigurationDefinitionField(
            "Default Username:",
            "This is the default username new channels will use.",
            "\"v1x1\"",
            ConfigType.STRING,
            [],
            Permission.READ_WRITE,
            "default_username",
            null
          )
        ],
        {}
      ),
      new V1x1ConfigurationDefinition(
        Permission.READ_WRITE,
        [
          new V1x1ConfigurationDefinitionField(
            "Custom Bot?",
            "This allows you to enter your own bot credentials to use.",
            "null",
            ConfigType.BOOLEAN,
            [],
            Permission.READ_WRITE,
            "custom_bot",
            null
          ),
          new V1x1ConfigurationDefinitionField(
            "Bot name:",
            "This allows you to enter a bot name other than v1x1.",
            "null",
            ConfigType.BOT_NAME,
            [],
            Permission.READ_WRITE,
            "bot_name",
            null
          ),
          new V1x1ConfigurationDefinitionField(
            "OAuth Token:",
            "What is your Twitch OAuth token for the bot?",
            "null",
            ConfigType.TWITCH_OAUTH,
            ["custom_bot"],
            Permission.READ_WRITE,
            "oauth_token",
            null
          )
        ],
        {}
      ),
      null
    )),
    new V1x1Module("link_purger", "Link Purger", "This module allows you to purge and/or timeout those who paste links.", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.READ_WRITE,
        [new V1x1ConfigurationDefinitionField(
          "Enabled?",
          "This controls whether this module is enabled on your channel.",
          "null",
          ConfigType.MASTER_ENABLE,
          [],
          Permission.READ_WRITE,
          "enabled",
          null
        )],
        {}
      ),
      null
    )),
    new V1x1Module("hello_world", "Hello World", "This module has a few commands which allow you to test basic functionality of v1x1.", new V1x1ConfigurationDefinitionSet(
      null,
      new V1x1ConfigurationDefinition(
        Permission.NONE,
        [new V1x1ConfigurationDefinitionField(
          "Enabled?",
          "This controls whether this module is enabled on your channel.",
          "null",
          ConfigType.MASTER_ENABLE,
          [],
          Permission.READ_WRITE,
          "enabled",
          null
        )],
        {}
      ),
      null
    )),
  ]
}

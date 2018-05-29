package tv.v1x1.modules.channel.voicelog;

import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
class VoiceLogListener implements EventListener {

    private final VoiceLog module;

    VoiceLogListener(final VoiceLog module) {
        this.module = module;
    }
}

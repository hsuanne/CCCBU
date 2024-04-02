/*====================================================================================*/
/*                                                                                    */
/*                        Copyright 2018 NXP                                          */
/*                                                                                    */
/*   All rights are reserved. Reproduction in whole or in part is prohibited          */
/*   without the written consent of the copyright owner.                              */
/*                                                                                    */
/*   NXP reserves the right to make changes without notice at any time. NXP makes     */
/*   no warranty, expressed, implied or statutory, including but not limited to any   */
/*   implied warranty of merchantability or fitness for any particular purpose,       */
/*   or that the use will not infringe any third party patent, copyright or trademark.*/
/*   NXP must not be liable for any loss or damage arising from its use.              */
/*                                                                                    */
/*====================================================================================*/
package com.nxp.cccbu;

import java.nio.ByteBuffer;

public class CommonConfigs {

    /* Generic App Configs */
    static final String PREF_NUMBER_OF_SESSION = "key_numOfSession";
    static final String PREF_SESSION_INDEX = "key_sessionIndex";
    static final String PREF_SESSION_ID = "key_sessionId";
    static final String PREF_SESSION_TYPE = "key_sessionType";
    static final String PREF_DEVICE_ROLE = "key_deviceRole";
    static final String PREF_DEVICE_TYPE = "key_deviceType";
    static final String PREF_SRC_ADDR = "key_srcAddr";
    static final String PREF_RANGING_DATA_SAMPLING_RATE = "key_rangingDataSamplingRate";
    static final String PREF_RANGING_METHOD = "key_rangingMethod";
    static final String PREF_STS_CONFIG = "key_stsConfig";
    static final String PREF_MULTI_NODE_MODE = "key_multiNodeMode";
    static final String PREF_RF_CHNNEL = "key_channel";
    static final String PREF_NUMBER_OF_CONTROLEE = "key_numberOfAnchors";
    static final String PREF_SLOT_DURATION = "key_slotLen";
    static final String PREF_RANGING_INTERVAL = "key_rangingInterval";
    static final String PREF_RANGING_DATA_NTF = "key_rangingDataNtf";
    static final String PREF_RANGING_DATA_NTF_PROXIMITY_FAR = "key_rangingDataNtfProximityFar";
    static final String PREF_RANGING_DATA_NTF_PROXIMITY_NEAR = "key_rangingDataNtfProximityNear";
    static final String PREF_STS_INDEX = "key_stsIndex";
    static final String PREF_MAC_TYPE = "key_macType";
    static final String PREF_AOA_RESULT_REQ = "key_aoaResultReq";
    static final String PREF_RFRAME_CONFIG = "key_ppduConfig";
    static final String PREF_PSDU_DATARATE = "key_psdu_datarate";
    static final String PREF_PREAMBLE_CODE_INDEX = "key_preambleIndex";
    static final String PREF_SFD_INDEX = "key_sfdIndex";
    static final String PREF_PREAMBLE_DURATION = "key_preambleDuration";
    static final String PREF_RANGING_ROUND_CONTROL = "key_ranging_round_phase_control";
    static final String PREF_RANGING_TIME_STRUCT = "key_ranging_time_struct";
    static final String PREF_TX_ADAPTIVE_POWER_PAYLOAD = "key_tx_adaptive_power_payload";
    static final String PREF_RESPONDER_SLOT_INDEX = "key_responder_slot_index";
    static final String PREF_PRF_MODE = "key_pref_prf_mode";
    static final String PREF_BPRF_PHR_DATA_RATE = "key_pref_bprf_phr_data_rate";
    static final String PREF_KEY_ROTATION = "key_key_rotation";
    static final String PREF_KEY_ROTATION_RATE = "key_key_rotation_rate";
    static final String PREF_SESSION_PRIORITY = "key_session_priority";
    static final String PREF_MAC_ADDRESS_MODE = "key_mac_address_mode";
    static final String PREF_NO_OF_STS_SEGMENTS = "key_no_of_sts_segments";
    static final String PREF_DEST_MAC_ADDRESS = "key_dest_mac_address";
    static final String PREF_RANGING_ROUND_HOPPING = "key_ranging_round_hopping";
    static final String PREF_UWB_INITIATION_TIME = "key_range_start_offset";
    static final String PREF_SLOTS_PER_RR = "key_slots_per_rr";
    static final String PREF_MAX_RR_RETRY = "key_max_rr_retry";
    static final String PREF_IN_BAND_TERMINATION_ATTEMPT_COUNT = "key_in_band_termination_attempt_count";
    static final String PREF_SUB_SESSION_ID = "key_sub_session_id";
    static final String PREF_RESULT_REPORT_CONFIG = "key_result_report_config";
    static final String PREF_APPLY_CONFIG_FROM_FILE = "key_apply_config_from_file";
    static final String PREF_APPLY_CALIBRATION_FROM_FILE = "key_apply_calibrationfrom_file";
    static final String PREF_BLINK_RANDOM_INTERVAL = "key_blinkRandomInterval";
    static final String PREF_TDOA_REPORT_FREQUENCY = "key_tdoaReportFreq";
    static final String PREF_MAX_NO_OF_MEASUREMENTS = "key_maxNoOfMeasurements";
    static final String PREF_STS_LENGTH = "key_stsLength";
    static final String PREF_BLOCK_STRIDE_LENGTH_MODE = "key_blockStrideLength";

    /* Vendor App Configs */
    static final String PREF_RX_MODE = "key_rxMode";
    static final String PREF_TOA_MODE = "key_toaMode";
    static final String PREF_TX_POWER = "key_txPower";
    static final String PREF_RX_ANTENNA_PAIR_SELECTION = "key_antennaPairSelection";
    static final String PREF_ANTENNA_PAIR_SELECTION_STR = "key_antennaPairSelectionStr";
    static final String PREF_RESET_CONFIGURATIONS = "key_reset_all_configurations";
    static final String PREF_MAX_CONTENTION_PHASE_LENGTH = "key_max_contention_phase_length";
    static final String PREF_CONTENTION_PHASE_UPDATE_LENGTH = "key_contention_phase_update_length";
    static final String PREF_SCHEDULED_MODE = "key_scheduled_mode";
    static final String PREF_CIR_CAPTURE_MODE = "key_cir_capture_mode";
    static final String PREF_SESSION_SYNC_ATTEMPTS = "key_session_sync_attempts";
    static final String PREF_SESSION_SCHED_ATTEMPTS = "key_session_sched_attempts";
    static final String PREF_SCHED_STATUS_NTF = "key_sched_ststus_ntf";
    static final String PREF_TX_POWER_DELTA_FCC = "key_tx_power_delta_fcc";
    static final String PREF_DUAL_AOA_PREAMBLE_STS = "key_dual_aoa_preamble_sts";
    static final String PREF_MAC_PAYLOAD_ENCRYPTION = "key_mac_payload_encryption";
    static final String PREF_RX_ANT_POLARIZATION = "key_rx_ant_polarization";
    static final String PREF_AUTH_TAG = "key_auth_tag";
    static final String PREF_ADAPTIVE_HOPPING_THRESHOLD = "key_adaptiveHoppingThreshold";
    static final String PREF_RX_ANTENNA_SELECTION_RFM = "key_rxAntennaSelectionRfm";
    static final String PREF_SESSION_INBAND_DATA_TX_BLOCKS = "key_sessionInbandDataTxBlocks";
    static final String PREF_SESSION_INBAND_DATA_RX_BLOCKS = "key_sessionInbandDataRxBlocks";
    static final String PREF_SUSPEND_RANGING = "key_suspendRanging";
    static final String PREF_DATA_TRANSFER_MODE = "key_dataTransferMode";


    /* CCC App Configs */
    static final String PREF_UWB_CONFIG_ID = "key_uwbConfigId";
    static final String PREF_PULSE_SHAPE_COMBO = "key_pulseShapeCombo";
    static final String PREF_PROTOCOL_VERSION = "key_protocolVersion";

    static final String RANGING_SESSION_TYPE = "0";
    static final String DATA_TRANSFER_SESSION_TYPE = "176";
    static final String CCC_RANGING_SESSION_TYPE = "160";

    static final int WAIT_TIME_OUT = 5000;
    static final byte CCC_SESSION_TYPE = (byte)0xA0;

    private static final String TAG = UwbManager.class.getSimpleName();
    private static final String APP_CONFIG_PATH = "/data/local/tmp/uwb_config.json";


    /* Generic App Configs */
    public static int sessionId = 0x01;
    static byte txPwrLevel = 0x00;
    static byte rangingRoundCntrl =2;
    static byte sfdId = 0x0, preambleDuration = 0x01;
    static byte rfChannelNo = 0x09;
    static byte preambleCodeIndex = 10;
    static byte rangingRoundUsage = 0x02;
    static byte stsConfig = 0x01;
    static byte multiNodeMode = 0x00;
    static byte noOfControlee = 0x01;
    static int rangingInterval = 96;
    static short slotDuration = 1200;
    static short rangingDataNtfFar = 0xFF;
    static short rangingDataNtfNear = 0;
    static int stsIndex = 0;
    static byte macType = 0x00;
    static byte aoaResultRequest = 0x01;
    static byte rngDataNtf = 0x01;
    static byte sessionType = (byte) 0xA0;
    static byte rframeConfig = 0x03;
    static byte psduDataRate = 0x0;
    static byte deviceType = 0x01;
    static byte deviceRole = 0x01;
    static short srcAddress = 0x00;
    static short[] macAddressList = new short[]{1};
    static int sessionState = 10;
    static byte samplingValue = 1;
    static short destAddress = 0x00;
    static byte blockStrideLength = 0x00;
    static byte responderSlotIndex = 1;
    static byte prfMode = 0;
    static byte scheduledMode = 1;
    static byte rangingTimeStruct = 1;
    static byte txAdaptivePower = 0;
    static byte keyRotation = 3;
    static byte keyRotationRate = 5;
    static byte sessionPriority = 50;
    static byte macAddressMode = 0;
    static byte noOfStsSegments = 0x01;
    static byte stsLength = 0x01;
    static short dstAddress = 0x00;
    static byte hoppingMode = 0x00;
    static int uwbInitiationTime = 0x64;
    static byte slotsPerRr = 12;
    static short maxRrRetry = 0x00;
    static byte inBandTermination = 0x01;
    static int subSessionId = 1111;
    static byte resultReportConfig = 0x01;
    static boolean loadConfigFromFile = false;
    static short blinkRandomInterval = 0;
    static short maxNoOfMeasurements = (short)0xFFFF;
    static short tdoaReportFreq = (short)0x0100;
    static byte bprfPhrDataRate = 0;

    /* Vendor App Configs */
    static byte rxMode = 0x00;
    static byte antennaPairSelection = 0x01;
    static byte toaMode = 0x02;
    static byte maxContentionPhaseLength = 50;
    static byte contentionPhaseUpdateLength = 5;
    static byte cirCaptureMode = 0x76;
    static byte sessionSyncAttempts = 0x03;
    static byte sessionSchedAttempts = 0x03;
    static byte schedStatusNtf = 0x00;
    static byte txPowerDeltaFcc = 0x00;
    static byte dualAoaPreambleSts = 0x00;
    static byte macPayloadEncryption = 0x01;
    static byte rxAntPolarization = 0x0;
    static byte adaptiveHoppingThreshold  = 1;
    static byte sessionInbandDataTxBlocks = 0;
    static byte sessionInbandDataRxBlocks = 0;
    static byte dataTransferMode = 1;
    static byte[] payload = null;
    private static byte STATUS_OK = 0x0;
    static byte authenticityTag = 0x00;
    static byte rxAntennaSelectionRfm  = 0;
    static byte suspendRanging = 0;

    /* CCC App Configs */
    static short uwbConfigId = 0x0;
    static byte pulseShapeCombo = 17;
    static byte cccConfigQuirks = 0x00;
    static short protocolVersion = 0x0100;

    static short convertToShort(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getShort();
    }

    static long convertToLong(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getLong();
    }

    public static byte[] getSrcMacAddress(short value) {
        ByteBuffer buffer;
        if (macAddressMode == 0) {
            buffer = ByteBuffer.allocate(2);
        } else {
            buffer = ByteBuffer.allocate(8);      // for ext mac address
        }
        buffer.putShort(value);
        return buffer.array();
    }

    public static byte[] getDstMacAddress(short[] valueArray) {
        ByteBuffer buffer;
        if (macAddressMode == 0) {
            buffer = ByteBuffer.allocate(2 * valueArray.length);
        } else {
            buffer = ByteBuffer.allocate(8 * valueArray.length);      // for ext mac address
        }
        for (int i = 0; i < valueArray.length; ++i) {
            buffer.putShort(valueArray[i]);
        }

        return buffer.array();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = (int) Math.max(Math.ceil(s.length() / 2.0), 1);
        int lenStr = s.length();
        byte[] data = new byte[len];
        for (int i = 0, j = 0; i < lenStr; i += 2, j++) {
            data[j] = (byte) Integer.parseInt(s.length() - i < 2 ? s.substring(i, lenStr) : s.substring(i, i + 2), 16);
        }
        return data;
    }





}

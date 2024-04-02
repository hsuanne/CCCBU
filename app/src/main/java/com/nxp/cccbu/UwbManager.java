package com.nxp.cccbu;

import static com.nxp.cccbu.CommonConfigs.*;

import android.content.Context;
import android.os.Binder;
import android.util.Log;

import com.nxp.uwb.UwbAdapter;
import com.nxp.uwb.UwbConfigParameterBuilder;
import com.nxp.uwb.UwbConfigParameterReturnStatus;
import com.nxp.uwb.UwbParameterTLV;
import com.nxp.uwb.extension.NxpUwbAdapter;
import com.nxp.uwb.extension.NxpUwbCccConfigParameterBuilder;
import com.nxp.uwb.extension.NxpUwbConfigParameterBuilder;
import com.nxp.uwb.extension.NxpUwbDebugConfigParameterBuilder;
import com.nxp.uwb.extension.NxpUwbDeviceCapabilityTlv;
import com.nxp.uwb.extension.NxpUwbDeviceTimeStamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UwbManager {
    public static short secureThreadLev = 0,threadSecureIsrLev = 0,threadNonSecureIsrLev = 0,threadShellLev = 0,threadPhyLev = 0,threadRangingLev = 0;
    public static byte CIR_LOG =0x00;
    public static byte DATA_LOGGER_LOG = 0x00;
    public static byte PSDU_DATA_LOG = 0x00;
    public static byte RFRAME_DATA_LOG = 0x00;
//    public static byte KDF_FEATURE = 0x00;
    private static boolean isCirEnabled = false;
    private static boolean isDataLoggerEnabled = false;
    private static boolean isPsduLogEnabled = false;
    private static boolean isRframeLogEnabled = false;
    private static boolean isKdfFeatureEnabled = false;
    public static final String TAG ="uwb";

    public static byte KDF_FEATURE = 0x00;
    static byte samplingValue = 1;


    private UwbAdapter mUwbAdapter;
    private NxpUwbAdapter mNxpUwbAdapter;
    public static Binder mBinder = new Binder();
    private Context mContext;


    private byte[] SLOT_BITMASK;//1 byte
    private byte[] SYNC_CODE_INDEX_BITMASK;//4 bytes
    private byte[] HOPPING_CONFIG_BITMASK; //1 byte
    private byte[] CHANNEL_BITMASK;//1 byte
    private byte[] SUPPORTED_PROTOCOL_VERSION; //2 bytes
    private byte[] SUPPORTED_UWB_CONFIG_ID; //2 bytes
    private byte[] UWBS_INBAND_DATA_BUFFER_BLOCK_SIZE; // 1 byte
    private byte[] UWBS_INBAND_DATA_MAX_BLOCKS;// 1 byte
    private byte[] SUPPORTED_PULSESHAPE_COMBO;// 3 bytes
    private byte   RAN_Multiplier;

    public byte getRAN_Multiplier() {
        return RAN_Multiplier;
    }

    public byte[] getSLOT_BITMASK() {
        return SLOT_BITMASK;
    }

    public byte[] getSYNC_CODE_INDEX_BITMASK() {
        return SYNC_CODE_INDEX_BITMASK;
    }

    public byte[] getHOPPING_CONFIG_BITMASK() {
        return HOPPING_CONFIG_BITMASK;
    }

    public byte[] getCHANNEL_BITMASK() {
        return CHANNEL_BITMASK;
    }

    public byte[] getSUPPORTED_PROTOCOL_VERSION() {
        return SUPPORTED_PROTOCOL_VERSION;
    }

    public byte[] getSUPPORTED_UWB_CONFIG_ID() {
        return SUPPORTED_UWB_CONFIG_ID;
    }

    public byte[] getUWBS_INBAND_DATA_BUFFER_BLOCK_SIZE() {
        return UWBS_INBAND_DATA_BUFFER_BLOCK_SIZE;
    }

    public byte[] getUWBS_INBAND_DATA_MAX_BLOCKS() {
        return UWBS_INBAND_DATA_MAX_BLOCKS;
    }

    public byte[] getSUPPORTED_PULSESHAPE_COMBO() {
        return SUPPORTED_PULSESHAPE_COMBO;
    }

    private static final UwbManager instance = new UwbManager(CCCBU.getInstance().getBaseContext());

    public static UwbManager getInstance(){
        return instance;
    }

    private UwbManager(Context context){
        mUwbAdapter = UwbAdapter.getUwbAdapter(context);
        mNxpUwbAdapter = NxpUwbAdapter.getNxpUwbAdapter(mUwbAdapter);
        mUwbAdapter.enable(mBinder);
        mContext = context;

        RAN_Multiplier = mNxpUwbAdapter.getRanMultiplier();

        for (NxpUwbDeviceCapabilityTlv tlv : mNxpUwbAdapter.getExtDeviceCapability()){
            switch (tlv.getType()){
                case "SLOT_BITMASK":{SLOT_BITMASK = tlv.getValue();}break;
                case "SYNC_CODE_INDEX_BITMASK":{SYNC_CODE_INDEX_BITMASK = tlv.getValue();}break;
                case "HOPPING_CONFIG_BITMASK":{HOPPING_CONFIG_BITMASK = tlv.getValue();}break;
                case "CHANNEL_BITMASK":{CHANNEL_BITMASK = tlv.getValue();}break;
                case "SUPPORTED_PROTOCOL_VERSION":{SUPPORTED_PROTOCOL_VERSION = tlv.getValue();}break;
                case "SUPPORTED_UWB_CONFIG_ID":{SUPPORTED_UWB_CONFIG_ID = tlv.getValue();}break;
                case "UWBS_INBAND_DATA_BUFFER_BLOCK_SIZE":{UWBS_INBAND_DATA_BUFFER_BLOCK_SIZE = tlv.getValue();}break;
                case "UWBS_INBAND_DATA_MAX_BLOCKS":{UWBS_INBAND_DATA_MAX_BLOCKS = tlv.getValue();}break;
                case "SUPPORTED_PULSESHAPE_COMBO":{SUPPORTED_PULSESHAPE_COMBO = tlv.getValue();}break;
            }
        }
    }

    public int getTime(){
        NxpUwbDeviceTimeStamp ts = mNxpUwbAdapter.getUwbDeviceTimeStamp();
        return ts.getSeconds()*1000000+ ts.getMicroSeconds();
    }

    public void getCapability(){
        List<NxpUwbDeviceCapabilityTlv> capability = mNxpUwbAdapter.getExtDeviceCapability();
        for(NxpUwbDeviceCapabilityTlv tlv :capability){
            Log.v("capability",tlv.getType()+":"+ByteUtils.bytes2String(tlv.getValue()));
        }
    }

    public byte setAppConfig(){
        byte status = 10;
        Log.i(TAG, "Session id for session " + sessionId);
        status = mUwbAdapter.sessionInit(sessionId, sessionType, mContext.getApplicationContext().getPackageName());

        if (status == 0) {
            Log.i(TAG, "Session Init is success");
        } else {
            Log.d(TAG, "session Init failure ");
            return status;
        }
        List<UwbParameterTLV> DebugConfig = new NxpUwbDebugConfigParameterBuilder()
                .setCirLog(CIR_LOG)
                .setDataLogger(DATA_LOGGER_LOG)
                .setPsduLog(PSDU_DATA_LOG)
                .setRframeLog(RFRAME_DATA_LOG)
                .setNonSecureIsrThreadLevel(threadNonSecureIsrLev)
                .setPhyThreadLevel(threadPhyLev)
                .setRangingThreadLevel(threadRangingLev)
                .setShellThreadLevel(threadShellLev)
                .setSecureThreadLevel(secureThreadLev)
                .setSecureIsrThreadLevel(threadSecureIsrLev).get();
        Log.d(TAG, "setDebugConfigurations " + CIR_LOG + secureThreadLev);
        if (mNxpUwbAdapter.setDebugConfigurations(sessionId, DebugConfig).getStatus() == 0) {
            Log.d(TAG, "setDebugConfigurations success ");
        } else
            Log.d(TAG, "setDebugConfigurations failure ");


        //KDF feature enable or disable
        byte[] array = {0x21, 0x03, 0x00, 0x09, (byte) sessionId,
                (byte) (sessionId >>> 8),
                (byte) (sessionId >>> 16),
                (byte) (sessionId >>> 24),
                0x01, (byte) 0xe3, 0x09, 0x01, KDF_FEATURE};
        byte[] resp = mNxpUwbAdapter.sendRawUci(array);
        Log.d(TAG, "send raw cmd resp : " + Arrays.toString(resp));

        status = mUwbAdapter.setRangingDataSamplingRate(sessionId, samplingValue);
        if (status == 0) Log.d(TAG, "set RangingData SamplingRate success ");
        else {
            Log.d(TAG, "setRangingData SamplingRate failure ");
            return status;
        }
        List<UwbParameterTLV> AppConfig;
        if (scheduledMode == 0x00 && deviceRole == 0x01) {     //quickshare initiator
            AppConfig = new UwbConfigParameterBuilder()
                    .setDeviceRole(deviceRole)
                    .setMultiNodeMode(multiNodeMode)
                    .setMacAddressMode(macAddressMode)
                    .setDeviceMacAddress(getSrcMacAddress(srcAddress))
                    .setDeviceType(deviceType)
                    .setRangingRoundControl(rangingRoundCntrl)
                    .setRangingDataNtf(rngDataNtf)
                    .setRangingRoundUsage(rangingRoundUsage)
                    .setRangingDataNtfProximityFar(rangingDataNtfFar)
                    .setRangingDataNtfProximityNear(rangingDataNtfNear)
                    .setStsConfig(stsConfig)
                    .setRframeConfig(rframeConfig)
                    .setRangingInterval(rangingInterval)
                    .setChannelNumber(rfChannelNo)
                    .setMacFcsType(macType)
                    .setAoaResultReq(aoaResultRequest)
                    .setSlotDuration(slotDuration)
                    .setPreambleCodeIndex(preambleCodeIndex)
                    .setStsIndex(stsIndex)
                    .setPreambleDuration(preambleDuration)
                    .setSfdId(sfdId)
                    .setPsduDataRate(psduDataRate)
                    .setRangingTimeStruct(rangingTimeStruct)
                    .setTxAdaptivePayloadPower(txAdaptivePower)
                    .setResponderSlotIndex(responderSlotIndex)
                    .setPrfMode(prfMode)
                    .setBprfPhrDataRate(bprfPhrDataRate)
                    .setScheduledMode(scheduledMode)
                    .setKeyRotation(keyRotation)
                    .setKeyRotationRate(keyRotationRate)
                    .setSessionPriority(sessionPriority)
                    .setNoOfStsSegments(noOfStsSegments)
                    .setHoppingMode(hoppingMode)
                    .setUwbInitiationTime(uwbInitiationTime)
                    .setMaxRrRetry(maxRrRetry)
                    .setSlotsPerRangingRound(slotsPerRr)
                    .setInBandTerminationAttemptCount(inBandTermination)
                    .setSubSessionId(subSessionId)
                    .setResultReportConfig(resultReportConfig)
                    .setTdoaReportFreq(tdoaReportFreq)
                    .setBlinkRandomInterval(blinkRandomInterval)
                    .setMaxNumberOfMeasurements(maxNoOfMeasurements)
                    .setStsLength(stsLength)
//                    .setBlockStrideLength((blockStrideLength))
                    .get();

        } else {
            if (scheduledMode == 0x00) {
                macAddressList[0] = dstAddress; //quickshare responder
            }
            AppConfig = new UwbConfigParameterBuilder()
                    .setDeviceRole(deviceRole)
                    .setMultiNodeMode(multiNodeMode)
                    .setNoOfControlee(noOfControlee)
                    .setMacAddressMode(macAddressMode)
                    .setDeviceMacAddress(getSrcMacAddress(srcAddress))
                    .setDstMacAddress(getDstMacAddress(macAddressList))
                    .setDeviceType(deviceType)
                    .setRangingRoundControl(rangingRoundCntrl)
                    .setRangingDataNtf(rngDataNtf)
                    .setRangingRoundUsage(rangingRoundUsage)
                    .setRangingDataNtfProximityFar(rangingDataNtfFar)
                    .setRangingDataNtfProximityNear(rangingDataNtfNear)
                    .setStsConfig(stsConfig)
                    .setRframeConfig(rframeConfig)
                    .setRangingInterval(rangingInterval)
                    .setChannelNumber(rfChannelNo)
                    .setMacFcsType(macType)
                    .setAoaResultReq(aoaResultRequest)
                    .setSlotDuration(slotDuration)
                    .setPreambleCodeIndex(preambleCodeIndex)
                    .setStsIndex(stsIndex)
                    .setPreambleDuration(preambleDuration)
                    .setSfdId(sfdId)
                    .setPsduDataRate(psduDataRate)
                    .setRangingTimeStruct(rangingTimeStruct)
                    .setTxAdaptivePayloadPower(txAdaptivePower)
                    .setResponderSlotIndex(responderSlotIndex)
                    .setPrfMode(prfMode)
                    .setBprfPhrDataRate(bprfPhrDataRate)
                    .setScheduledMode(scheduledMode)
                    .setKeyRotation(keyRotation)
                    .setKeyRotationRate(keyRotationRate)
                    .setSessionPriority(sessionPriority)
                    .setNoOfStsSegments(noOfStsSegments)
                    .setHoppingMode(hoppingMode)
                    .setUwbInitiationTime(uwbInitiationTime)
                    .setMaxRrRetry(maxRrRetry)
                    .setSlotsPerRangingRound(slotsPerRr)
                    .setInBandTerminationAttemptCount(inBandTermination)
                    .setSubSessionId(subSessionId)
                    .setResultReportConfig(resultReportConfig)
                    .setTdoaReportFreq(tdoaReportFreq)
                    .setBlinkRandomInterval(blinkRandomInterval)
                    .setMaxNumberOfMeasurements(maxNoOfMeasurements)
                    .setStsLength(stsLength)
                    //.setBlockStrideLength((blockStrideLength))
                    .get();
        }


        status = (byte) mUwbAdapter.setAppConfigurations(sessionId, AppConfig).getStatus();
        if (status == 0) Log.d(TAG, "set AppConfigurations success ");
        else {
            Log.d(TAG, "set AppConfigurations failure " + status);
            return status;
        }


            List<UwbParameterTLV> cccAppConfig = new NxpUwbCccConfigParameterBuilder()
                    .setUwbConfigId(uwbConfigId)
                    .setPulseShapeCombo(pulseShapeCombo)
                    .setRangingProtocolVersion(protocolVersion)
                    .setCccConfigQuirks(cccConfigQuirks)
                    .get();


            status = (byte) mNxpUwbAdapter.setCccAppConfigurations(sessionId, cccAppConfig).getStatus();

            if (status == 0) Log.d(TAG, "setCccAppConfigurations success");
            else {
                Log.d(TAG, "setCccAppConfigurations failed");
                return status;
            }



        List<UwbParameterTLV> ExtAppConfig = new NxpUwbConfigParameterBuilder()
                .setToaMode(toaMode)
                .setCirCaptureMode(cirCaptureMode)
                .setSessionSyncAttempts(sessionSyncAttempts)
                .setSessionSchedAttempts(sessionSchedAttempts)
                .setScheduledStatusNtf(schedStatusNtf)
                .setTxPowerDeltaFcc(txPowerDeltaFcc)
                .setDualAoaPreambleSts(dualAoaPreambleSts)
                .setMacPayloadEncryption(macPayloadEncryption)
                .setRxAntennaPolarizationOption(rxAntPolarization)
                //.setRxAntennaSelectionRfm(rxAntennaSelectionRfm)
                //.setSessionInbandDataTxBlocks(sessionInbandDataTxBlocks)
                //.setSessionInbandDataRxBlocks(sessionInbandDataRxBlocks)
                .setAdaptiveHoppingThreshold(adaptiveHoppingThreshold)
                .setMaxContentionPhaseLength(maxContentionPhaseLength)
                .setContentionPhaseUpdateLength(contentionPhaseUpdateLength)
                .setRxMode(rxMode)
                .setAntennaPairSelection(antennaPairSelection)
                .setAuthenticityTag(authenticityTag)
                .get();
        status = (byte) mNxpUwbAdapter.setExtAppConfigurations(sessionId, ExtAppConfig).getStatus();
        if (status == 0) {
            Log.d(TAG, "setExtAppConfigurations success ");
        } else {
            return status;
        }
        return status;
    }

    public byte startRanging() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte status = 10;

        status = mUwbAdapter.rangingStart(sessionId);
        if (status == 0) {
            Log.i(TAG, "ranging Start success");
        } else {
            Log.d(TAG, "rangingStart failure " + status);
        }
        return status;
    }

    public  byte sessionDeInitialize() {
        byte status = 10;
        status = mUwbAdapter.sessionDeInit(sessionId);
        if (status == 0) {
            Log.d(TAG, "session DeInit success ");
        } else {
            Log.d(TAG, "session DeInit failure ");
            return status;
        }
        return status;
    }

    public byte stopRanging() {
        byte status = 10;
        status = mUwbAdapter.rangingStop(sessionId);
        return status;
    }
}

import React, { useCallback, useRef, useState } from 'react';
import styled from 'styled-components';
import { Flex, ProgressCircle, View } from '@adobe/react-spectrum';
import { ImageCarousel } from '../image-carousel/ImageCarousel';

const convertFileToBase64 = (file) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
    });
};

export const ImageUploader = ({
    images,
    setImages,
    zeroState,
    multiple,
    hideCarousel,
}) => {
    const inputRef = useRef();
    const [isLoading, setIsLoading] = useState(false);
    const triggerUploadDialog = () => inputRef?.current?.click();

    const onFileChange = useCallback(async (event) => {
        setIsLoading(true);
        const imageUploads = await Promise.all(
            [...event.target.files].map(convertFileToBase64),
        );
        setIsLoading(false);
        setImages(imageUploads);
    }, []);

    return (
        <View width="100%" height="100%">
            {isLoading ? (
                <Flex
                    width="100%"
                    height="100%"
                    justifyContent="center"
                    alignItems="center"
                >
                    <ProgressCircle isIndeterminate size="L" />
                </Flex>
            ) : (
                <>
                    {(!images || !images.length) && (
                        <ZeroStateWrapper
                            width="100%"
                            height="100%"
                            onClick={triggerUploadDialog}
                        >
                            {zeroState}
                            <input
                                type="file"
                                accept="image/*"
                                hidden
                                multiple={multiple}
                                ref={inputRef}
                                onChange={onFileChange}
                            />
                        </ZeroStateWrapper>
                    )}
                    {images && !hideCarousel && (
                        <ImageCarousel images={images} setImages={setImages} />
                    )}
                </>
            )}
        </View>
    );
};

const ZeroStateWrapper = styled.div`
    width: 100%;
    height: 100%;
`;

import React from 'react';
import Image from '@spectrum-icons/workflow/Image';
import styled from 'styled-components';

export const ImagePlaceholder = ({ size }) => {
    return (
        <PlaceholderWrapper>
            <Image size={size} />
        </PlaceholderWrapper>
    );
};

const PlaceholderWrapper = styled.div`
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(
        --spectrum-alias-background-color-gray-300,
        var(
            --spectrum-global-color-gray-300,
            var(--spectrum-semantic-gray-300-color-background)
        )
    );
    border-color: var(
        --spectrum-alias-border-color-gray-500,
        var(
            --spectrum-global-color-gray-500,
            var(--spectrum-semantic-gray-500-color-border)
        )
    );
    border-radius: var(--spectrum-alias-border-radius-medium);
    border-style: solid;
    border-width: var(--spectrum-alias-border-size-thin);
    box-sizing: border-box;
`;

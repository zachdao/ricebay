import React, { useCallback } from 'react';
import { Flex, Heading } from '@adobe/react-spectrum';
import styled, { keyframes } from 'styled-components';
import { useNavigate } from 'react-router-dom';

export const Sidebar = ({ dismiss }) => {
    const navigate = useNavigate();

    const navigateAndDismiss = useCallback(
        (route) => {
            dismiss();
            navigate(route);
        },
        [dismiss, navigate],
    );

    return (
        <Wrapper>
            <Flex direction="column" width="100%">
                <MenuItem onClick={() => navigateAndDismiss('/')}>
                    <Heading level={3}>Home</Heading>
                </MenuItem>
                <MenuItem onClick={() => navigateAndDismiss('/account')}>
                    <Heading level={3}>Account</Heading>
                </MenuItem>
            </Flex>
        </Wrapper>
    );
};

const MenuItem = styled.div`
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: start;
    padding-left: 25px;
    cursor: pointer;
    &:hover {
        background-color: var(
            --spectrum-alias-background-color-gray-200,
            var(
                --spectrum-global-color-gray-200,
                var(--spectrum-semantic-gray-200-color-background)
            )
        );
    }
`;

const SlideOpen = keyframes`
    from {
        transform: translateX(-100%)
    }
    
    to {
        transform: translateX(0%)
    }
`;

const Wrapper = styled.div`
    width: 95%;
    height: 100%;
    box-shadow: grey 2px 9px 11px 0px;
    animation: ${SlideOpen} 0.125s ease-in-out 1 alternate;
`;

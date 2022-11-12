import React, { useState } from 'react';
import { Flex, Button } from '@adobe/react-spectrum';
import styled from 'styled-components';

export const Popover = ({
    triggerText,
    triggerWidth,
    width,
    isOpen,
    onPress,
    children,
}) => {
    const [isOpenControlled, setIsOpenControlled] = useState(false);
    return (
        <Flex
            direction="column"
            width={triggerWidth || '100%'}
            position="relative"
        >
            <Button
                variant="primary"
                width="100%"
                isDisabled={isOpen === undefined ? isOpenControlled : isOpen}
                onPress={() =>
                    isOpen === undefined
                        ? setIsOpenControlled((value) => !value)
                        : onPress()
                }
            >
                {triggerText}
            </Button>
            <PopoverContent
                isOpen={isOpen === undefined ? isOpenControlled : isOpen}
                width={width || '100%'}
            >
                {children}
            </PopoverContent>
        </Flex>
    );
};

const PopoverContent = styled.div`
    display: ${(props) => (props.isOpen ? 'flex' : 'none')};
    position: absolute;
    background: white;
    box-shadow: grey 2px 2px 4px;
    width: ${(props) => props.width};
    min-height: 50px;
    top: 37px;
    z-index: 1;
    align-items: center;
    justify-content: start;
    flex-direction: column;
    border-radius: 5px;
    border: whitesmoke solid 1px;
`;
